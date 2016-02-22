package com.noiseapps.itassistant.fragment.stash;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.adapters.stash.CommitListAdapter;
import com.noiseapps.itassistant.connector.StashConnector;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.atlassian.PagedApiModel;
import com.noiseapps.itassistant.model.stash.commits.Commit;
import com.noiseapps.itassistant.model.stash.projects.StashProject;
import com.noiseapps.itassistant.utils.AuthenticatedPicasso;
import com.noiseapps.itassistant.utils.EndlessRecyclerOnScrollListener;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

@EFragment(R.layout.fragment_commit_list)
public class CommitListFragment extends Fragment {

    private final List<Commit> commits = new ArrayList<>();
    @ViewById
    RecyclerView commitList;
    @ViewById
    LinearLayout noProjectData, fetchingDataProgress, commitListWrapper;
    @ViewById
    TextView errorMessageTextView;
    @ViewById
    MaterialProgressBar downloadingMoreData;
    @FragmentArg
    String repoSlug;
    @FragmentArg
    StashProject project;
    @FragmentArg
    BaseAccount account;
    @Bean
    StashConnector connector;

    private int start;
    private CommitListAdapter adapter;

    @AfterViews
    void init() {
        fetchingDataProgress.setVisibility(View.VISIBLE);
        setupToolbar();
        setupListView();
        downloadMoreItems();
    }

    private void setupListView() {
        adapter = new CommitListAdapter(getActivity(), AuthenticatedPicasso.getAuthPicasso(getActivity(), account), commits);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        commitList.setLayoutManager(layoutManager);
        commitList.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                downloadMoreItems();
            }
        });
        commitList.setAdapter(adapter);
    }

    private void setupToolbar() {
        final ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setHomeButtonEnabled(true);
            supportActionBar.setTitle(getString(R.string.commitsForProject, project.getName()));
            setHasOptionsMenu(true);
        }
    }

    private void downloadMoreItems() {
        downloadingMoreData.setVisibility(View.VISIBLE);
        connector.getCommitsPage(project.getKey(), repoSlug, start).subscribe(this::addToList, this::showError);
    }

    private void addToList(PagedApiModel<Commit> commitPagedApiModel) {
        Logger.d(String.valueOf(commitPagedApiModel.getValues().size()));
        fetchingDataProgress.setVisibility(View.GONE);
        downloadingMoreData.setVisibility(View.GONE);
        commitList.setVisibility(View.VISIBLE);
        start = commitPagedApiModel.getStart() + commitPagedApiModel.getSize();
        commits.addAll(commitPagedApiModel.getValues());
        adapter.notifyDataSetChanged();
    }

    private void showError(Throwable throwable) {
        Logger.e(throwable, throwable.getMessage());
        noProjectData.setVisibility(View.VISIBLE);
        downloadingMoreData.setVisibility(View.GONE);
        commitList.setVisibility(View.GONE);
        errorMessageTextView.setText(R.string.failedToFetchCommitList);
    }
}
