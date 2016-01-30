package com.noiseapps.itassistant.fragment.stash;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.connector.StashConnector;
import com.noiseapps.itassistant.model.stash.projects.StashProject;
import com.noiseapps.itassistant.model.stash.pullrequests.PullRequest;
import com.noiseapps.itassistant.utils.views.MyFabProgressCircle;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_pr_list)
public class PullRequestListFragment extends Fragment {

    @FragmentArg
    StashProject stashProject;

    @FragmentArg
    String repoSlug;

    @Bean
    StashConnector connector;

    @ViewById
    TabLayout tabLayout;

    @ViewById
    ViewPager viewPager;
    @ViewById
    MyFabProgressCircle fabProgressCircle;
    @ViewById
    View fetchingDataProgress, noProjectData, tabView;
    @ViewById
    TextView errorMessageTextView;
    private List<PullRequest> pullRequests;

    @AfterViews
    void init() {
        showProgress();
        downloadPullRequests();
        setupToolbar();
    }

    private void setupToolbar() {
        final ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setHomeButtonEnabled(true);
            supportActionBar.setTitle(getString(R.string.prsForProject, stashProject.getName()));
            setHasOptionsMenu(true);
        }
    }

    @Background
    void downloadPullRequests() {
        pullRequests = connector.getPullRequests(stashProject.getKey(), repoSlug);
        if (pullRequests.size() > 0) {
            onPullRequestsDownloaded();
        } else {
            showDownloadError();
        }
    }

    private void showDownloadError() {
        hideProgress();
        noProjectData.setVisibility(View.VISIBLE);
    }

    @UiThread
    void hideProgress() {
        fetchingDataProgress.setVisibility(View.GONE);
    }

    private void showProgress() {
        tabView.setVisibility(View.GONE);
        noProjectData.setVisibility(View.GONE);
        fetchingDataProgress.setVisibility(View.VISIBLE);
    }

    @UiThread
    void onPullRequestsDownloaded() {
        hideProgress();
        tabView.setVisibility(View.VISIBLE);
        final PagerAdapter adapter = new PullRequestsPagerAdapter();
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                onPageSelected(position);
            }

            @Override
            public void onPageSelected(int position) {
                if(fabProgressCircle.isCollapsed()) {
                    fabProgressCircle.expand();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class PullRequestsPagerAdapter extends FragmentPagerAdapter {
        private static final String STATUS_MERGED = "MERGED";
        private static final String STATUS_DECLINED = "DECLINED";
        private static final String STATUS_OPEN = "OPEN";
        private final Fragment[] fragments = new Fragment[3];
        private final String[] pullRequestCategories;

        public PullRequestsPagerAdapter() {
            super(getChildFragmentManager());
            pullRequestCategories = getActivity().getResources().getStringArray(R.array.prCategories);
            fillFragments();
        }

        private void fillFragments() {
            final ArrayList<PullRequest> open = new ArrayList<>(),
                    merged = new ArrayList<>(),
                    declined = new ArrayList<>();

            for (PullRequest pullRequest : pullRequests) {
                final String state = pullRequest.getState();
                if (STATUS_MERGED.equalsIgnoreCase(state)) {
                    merged.add(pullRequest);
                } else if (STATUS_DECLINED.equalsIgnoreCase(state)) {
                    declined.add(pullRequest);
                } else if (STATUS_OPEN.equalsIgnoreCase(state)) {
                    open.add(pullRequest);
                }
            }

            fragments[0] = PullRequestCategory_.builder().pullRequests(open).build();
            fragments[1] = PullRequestCategory_.builder().pullRequests(merged).build();
            fragments[2] = PullRequestCategory_.builder().pullRequests(declined).build();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pullRequestCategories[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }
}
