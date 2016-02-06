package com.noiseapps.itassistant;

import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.noiseapps.itassistant.fragment.stash.CommitListFragment_;
import com.noiseapps.itassistant.fragment.stash.BranchListFragment_;
import com.noiseapps.itassistant.fragment.stash.PullRequestListFragment;
import com.noiseapps.itassistant.fragment.stash.PullRequestListFragment_;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.stash.projects.StashProject;
import com.noiseapps.itassistant.utils.annotations.StashActions;

import org.androidannotations.annotations.AfterViews;
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

    @Extra
    @StashActions
    int stashAction;

    @Extra
    String repoSlug;
    @Extra
    StashProject project;
    @Extra
    BaseAccount baseAccount;


    @ViewById
    Toolbar toolbar;

    @AfterViews
    void init() {
        setTablet();
        setSupportActionBar(toolbar);
        showActionFragment();
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
        switch (stashAction) {
            case ACTION_SOURCE:
                break;
            case ACTION_BRANCHES:
                fragment = BranchListFragment_.builder().project(project).repoSlug(repoSlug).build();
                replaceFragment(fragment);
                break;
            case ACTION_COMMITS:
                fragment = CommitListFragment_.builder().project(project).repoSlug(repoSlug).build();
                replaceFragment(fragment);
                break;
            case ACTION_PULL_REQUESTS:
                fragment = PullRequestListFragment_.builder().stashProject(project).repoSlug(repoSlug).baseAccount(baseAccount).build();
                replaceFragment(fragment);
                break;
            default:
                throw new UnsupportedOperationException("Wrong action code");
        }

    }

    @OptionsItem(android.R.id.home)
    void onHome() {
        onBackPressed();
    }

    private void replaceFragment(@NonNull Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }

}
