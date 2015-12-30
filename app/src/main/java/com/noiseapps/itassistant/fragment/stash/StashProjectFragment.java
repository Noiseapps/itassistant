package com.noiseapps.itassistant.fragment.stash;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.connector.StashConnector;
import com.noiseapps.itassistant.connector.StashConnector_;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.stash.projects.ProjectRepos;
import com.noiseapps.itassistant.model.stash.projects.StashProject;
import com.noiseapps.itassistant.model.stash.projects.StashRepoMeta;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EFragment(R.layout.fragment_stash_project)
public class StashProjectFragment extends Fragment {

    @ViewById
    LinearLayout fetchingDataProgress, noProjectData, stashMenuRoot;
    @ViewById
    FrameLayout rootView;


    StashProject stashProject;
    private BaseAccount baseAccount;

    @Bean
    StashConnector stashConnector;

    @AfterViews
    void init() {
        configureToolbar(true);
    }

    private void configureToolbar(boolean showCustomView) {
        final ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setCustomView(R.layout.layout_toolbar_spinner);
            supportActionBar.setDisplayShowCustomEnabled(showCustomView);
            supportActionBar.setDisplayShowTitleEnabled(!showCustomView);
        }
    }

    @Override
    public void onDetach() {
        configureToolbar(false);
        super.onDetach();
    }

    public void setProject(StashProject jiraProject, BaseAccount baseAccount) {
        stashProject = jiraProject;
        this.baseAccount = baseAccount;
        if (stashConnector == null) {
            stashConnector = StashConnector_.getInstance_(getContext());
        }
        stashConnector.getProjectRepos(stashProject.getKey()).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(this::onReposDownloaded, this::onDownloadFailed);
        Logger.d(String.valueOf(stashProject));
    }


    private void onDownloadFailed(Throwable throwable) {
        showError();
    }

    private void showError() {
        hideProgress();
        Snackbar.make(rootView, R.string.failedToFetchDetails, Snackbar.LENGTH_LONG).show();
    }

    private void hideProgress() {
        fetchingDataProgress.setVisibility(View.GONE);
    }

    private void onReposDownloaded(ProjectRepos repos) {
        final ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setCustomView(R.layout.layout_toolbar_spinner);
            final Spinner toolbarSpinner = (Spinner) supportActionBar.getCustomView().findViewById(R.id.spinner);
            final ArrayAdapter<StashRepoMeta> adapter = new ArrayAdapter<>(getActivity(), R.layout.item_spinner_textonly, R.id.title, repos.getRepos());
            toolbarSpinner.setAdapter(adapter);
            toolbarSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    final StashRepoMeta item = adapter.getItem(position);
                    loadProjectDetails(item);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            supportActionBar.setDisplayShowCustomEnabled(true);
        }
    }

    private void loadProjectDetails(StashRepoMeta item) {
        stashConnector.getRepoDetails(stashProject.getKey(), item.getSlug()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io()).
                subscribe(this::onDetailsDownloaded, this::onDownloadFailed);
        // todo load and display data
    }

    private void onDetailsDownloaded(Object projectRepos) {
    }

    private void showProgress() {
        fetchingDataProgress.setVisibility(View.VISIBLE);
    }

}
