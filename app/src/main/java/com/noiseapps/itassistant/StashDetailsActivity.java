package com.noiseapps.itassistant;

import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.noiseapps.itassistant.fragment.stash.CommitListFragment_;
import com.noiseapps.itassistant.fragment.stash.BranchListFragment_;
import com.noiseapps.itassistant.fragment.stash.PullRequestDetailsFragment;
import com.noiseapps.itassistant.fragment.stash.PullRequestDetailsFragment_;
import com.noiseapps.itassistant.fragment.stash.PullRequestListFragment;
import com.noiseapps.itassistant.fragment.stash.PullRequestListFragment_;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.stash.projects.StashProject;
import com.noiseapps.itassistant.model.stash.pullrequests.PullRequest;
import com.noiseapps.itassistant.utils.annotations.StashActions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_stash_details)
public class StashDetailsActivity extends AppCompatActivity {

    public static final int ACTION_SOURCE = 0;
    public static final int ACTION_COMMITS = 1;
    public static final int ACTION_BRANCHES = 2;
    public static final int ACTION_PULL_REQUESTS = 3;
    public static final int ACTION_PULL_REQUEST_DETAILS = 4;

    @Extra
    @StashActions
    int stashAction;

    @Extra
    String repoSlug;
    @Extra
    StashProject project;
    @Extra
    BaseAccount baseAccount;
    @Extra
    PullRequest pullRequest;


    @ViewById
    Toolbar toolbar;
    @Bean
    AnalyticsTrackers tracker;

    @AfterViews
    void init() {
        setTablet();
        setSupportActionBar(toolbar);
        showActionFragment();


        tracker.sendScreenVisit(AnalyticsTrackers.SCREEN_STASH_DETAILS);
        tracker.sendEvent(AnalyticsTrackers.SCREEN_STASH_DETAILS,
                AnalyticsTrackers.CATEGORY_STASH_SCREEN_DETAILS,
                "stashDetailsAction",
                String.valueOf(stashAction));
    }

    private void setTablet() {
        if (getResources().getBoolean(R.bool.tabletSize)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
    }


    private void showActionFragment() {
        Fragment fragment;
        fragment = getDetailsFragment();
        if(fragment != null) {
            replaceFragment(fragment);
        }
    }

    @Nullable
    private Fragment getDetailsFragment() {
        final Fragment fragment;
        switch (stashAction) {
            case ACTION_SOURCE:
                fragment = null;
                break;
            case ACTION_BRANCHES:
                fragment = BranchListFragment_.builder().project(project).repoSlug(repoSlug).build();
                break;
            case ACTION_COMMITS:
                fragment = CommitListFragment_.builder().project(project).repoSlug(repoSlug).build();
                break;
            case ACTION_PULL_REQUESTS:
                fragment = PullRequestListFragment_.builder().
                        stashProject(project).
                        repoSlug(repoSlug).
                        baseAccount(baseAccount).build();
                break;
            case ACTION_PULL_REQUEST_DETAILS:
                fragment = PullRequestDetailsFragment_.builder().
                        stashProject(project).
                        repoSlug(repoSlug).
                        pullRequestId(pullRequest.getId()).
                        account(baseAccount).build();
                break;
            default:
                throw new UnsupportedOperationException("Wrong action code");
        }
        return fragment;
    }

    @OptionsItem(android.R.id.home)
    void onHome() {
        onBackPressed();
    }

    private void replaceFragment(@NonNull Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }

}
