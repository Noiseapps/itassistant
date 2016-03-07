package com.noiseapps.itassistant.fragment.stash;


import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.annimon.stream.Stream;
import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.connector.StashConnector;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.stash.general.StashUser;
import com.noiseapps.itassistant.model.stash.projects.StashProject;
import com.noiseapps.itassistant.model.stash.pullrequests.MergeStatus;
import com.noiseapps.itassistant.model.stash.pullrequests.PullRequest;
import com.noiseapps.itassistant.model.stash.pullrequests.Veto;
import com.noiseapps.itassistant.model.stash.pullrequests.details.DiffBase;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import rx.Observable;

@EFragment(R.layout.fragment_pull_request_details)
public class PullRequestDetailsFragment extends Fragment {

    @FragmentArg
    String repoSlug;

    @FragmentArg
    StashProject stashProject;

    @FragmentArg
    int pullRequestId;

    @FragmentArg
    BaseAccount account;

    @ViewById
    TextView conflicts, fetchError;

    @ViewById
    LinearLayout fetchingDataProgress, contentView;
    @ViewById
    TabLayout tabLayout;
    @ViewById
    ViewPager viewPager;
    @Bean
    StashConnector connector;

    private PullRequest pullRequest;
    private DiffBase diffBase;
    private MergeStatus mergeStatus;

    @AfterViews
    void init() {
        showProgress();
        setupToolbar();
        connector.getPullRequest(stashProject.getKey(), repoSlug, pullRequestId).
                subscribe(this::onPullRequestReceived, this::onDownloadError);
    }

    private void onPullRequestReceived(PullRequest pullRequest) {
        this.pullRequest = pullRequest;
        connector.getPullRequestDetails(stashProject.getKey(), repoSlug, pullRequest.getFromRef().getLatestChangeset(), pullRequest.getToRef().getLatestChangeset()).
                subscribe(this::showPullRequestDetails, this::onDownloadError);
    }

    private void setupToolbar() {
        final ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setHomeButtonEnabled(true);
            supportActionBar.setTitle(getString(R.string.pullRequestDetails, pullRequestId));
            setHasOptionsMenu(true);
        }
    }

    private void showPullRequestDetails(DiffBase diffBase) {
        this.diffBase = diffBase;
        if(pullRequest.getState().equalsIgnoreCase(PullRequest.STATUS_OPEN)){
            connector.checkPullRequestStatus(stashProject.getKey(), repoSlug, pullRequest.getId()).
                subscribe(this::onStatusDownloaded, this::onDownloadError);
        } else {
            fillData();
        }
    }

    private void onStatusDownloaded(MergeStatus mergeStatus) {
        this.mergeStatus = mergeStatus;
        if (mergeStatus.isConflicted()) {
            hideAll();
            conflicts.setVisibility(View.VISIBLE);
        } else {
            fillData();
        }
    }

    private void fillData() {
        Logger.d("fill data");
        hideAll();
        contentView.setVisibility(View.VISIBLE);
        viewPager.setAdapter(new DetailsPagerAdapter());
        tabLayout.setupWithViewPager(viewPager);
    }

    private void hideAll() {
        fetchingDataProgress.setVisibility(View.GONE);
        conflicts.setVisibility(View.GONE);
        contentView.setVisibility(View.GONE);
        fetchError.setVisibility(View.GONE);
    }

    private void showProgress() {
        fetchingDataProgress.setVisibility(View.VISIBLE);
        conflicts.setVisibility(View.GONE);
        contentView.setVisibility(View.GONE);
        fetchError.setVisibility(View.GONE);
    }

    private void onDownloadError(Throwable throwable) {
        Logger.e(throwable, throwable.getMessage());
        hideAll();
        fetchError.setVisibility(View.VISIBLE);

    }

    private class DetailsPagerAdapter extends FragmentStatePagerAdapter {

        private final String[] tabTitles = getResources().getStringArray(R.array.prTabs);
        private final Fragment[] items = new Fragment[2];

        public DetailsPagerAdapter() {
            super(getChildFragmentManager());

            initItems();
        }

        private void initItems() {
            items[0] = PullRequestOverviewFragment_.builder().
                    account(account).
                    pullRequest(pullRequest).
                    mergeStatus(mergeStatus).
                    repoSlug(repoSlug).
                    stashProject(stashProject).build();

            items[1] = PullRequestDiffFragment_.builder().diffBase(diffBase).build();
        }

        @Override
        public Fragment getItem(int position) {
            return items[position];
        }

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }


}
