package com.noiseapps.itassistant.fragment.stash;

import android.app.ProgressDialog;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.noiseapps.itassistant.BuildConfig;
import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.connector.StashConnector;
import com.noiseapps.itassistant.dialogs.CreatePullRequestDialog;
import com.noiseapps.itassistant.dialogs.CreatePullRequestDialog_;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.stash.branches.BranchModel;
import com.noiseapps.itassistant.model.stash.general.StashUser;
import com.noiseapps.itassistant.model.stash.projects.StashProject;
import com.noiseapps.itassistant.model.stash.pullrequests.PullRequest;
import com.noiseapps.itassistant.utils.views.MyFabProgressCircle;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
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

    @FragmentArg
    BaseAccount baseAccount;

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
    private ProgressDialog progressDialog;
    private List<StashUser> users;
    private List<BranchModel> branches;

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
        if(isAdded() && getActivity() != null) {
            if (pullRequests.size() > 0) {
                onPullRequestsDownloaded();
            } else {
                showDownloadError();
            }
        }
    }

    @UiThread
    void showDownloadError() {
        hideProgress();
        noProjectData.setVisibility(View.VISIBLE);
    }

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
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                onPageSelected(position);
            }

            @Override
            public void onPageSelected(int position) {
                if (fabProgressCircle.isCollapsed()) {
                    fabProgressCircle.expand();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Click(R.id.fabProgressCircle)
    void onAddPullRequest() {
        if(branches == null && users == null) {
            showDetailsFetchProgress();
            downloadCreatePullRequestData();
        } else {
            onBranchesDownloaded(branches, users);
        }
    }

    private void showDetailsFetchProgress() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.fetchingBranches));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    @Background
    void downloadCreatePullRequestData() {
        branches = connector.getBranches(stashProject.getKey(), repoSlug);
        users = connector.getUsers();
        if(isAdded() && getActivity() != null) {
            if (!branches.isEmpty() && !users.isEmpty()) {
                onBranchesDownloaded(branches, users);
            } else {
                onDownloadError();
            }
        }
    }

    @UiThread
    void onBranchesDownloaded(List<BranchModel> branches, List<StashUser> users) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        CreatePullRequestDialog_.getInstance_(getActivity()).init(branches, users, baseAccount, this::onCreatePullRequest);
    }

    private void onCreatePullRequest(PullRequest pullRequest, CreatePullRequestDialog dialog) {
        if(BuildConfig.DEBUG) {
            onPrCreated(PullRequest.spoof(), dialog);
        } else {
            connector.createPullRequest(stashProject.getKey(), repoSlug, pullRequest).
                    subscribe(request -> onPrCreated(request, dialog),
                            throwable -> onCreateError(throwable, dialog));
        }
    }

    private void onPrCreated(PullRequest response, CreatePullRequestDialog dialog) {
        dialog.getAlertDialog().dismiss();
        Snackbar.make(viewPager, getString(R.string.pullRequestCreated, response.getId()), Snackbar.LENGTH_LONG).show();
        pullRequests.add(response);
        final PullRequestsPagerAdapter adapter = (PullRequestsPagerAdapter) viewPager.getAdapter();
        adapter.fillFragments();
        viewPager.setAdapter(adapter);
    }

    private void onCreateError(Throwable throwable, CreatePullRequestDialog dialog) {
        dialog.onCreateError();
        Logger.e(throwable, throwable.getMessage());
    }

    @UiThread
    void onDownloadError() {
        Snackbar.make(viewPager, R.string.failedToFetchDetails, Snackbar.LENGTH_LONG).show();
    }

    private class PullRequestsPagerAdapter extends FragmentPagerAdapter {
        private static final String STATUS_MERGED = "MERGED";
        private static final String STATUS_DECLINED = "DECLINED";
        private static final String STATUS_OPEN = "OPEN";
        private final PullRequestCategory[] fragments = new PullRequestCategory[3];
        private final String[] pullRequestCategories;

        public PullRequestsPagerAdapter() {
            super(getChildFragmentManager());
            pullRequestCategories = getActivity().getResources().getStringArray(R.array.prCategories);
            fillFragments();
        }

        void fillFragments() {
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

            setupFragments(open, merged, declined);
        }

        private void setupFragments(ArrayList<PullRequest> open, ArrayList<PullRequest> merged, ArrayList<PullRequest> declined) {
            if(fragments[0] == null) {
                fragments[0] = PullRequestCategory_.builder().pullRequests(open).build();
            } else {
                fragments[0].setPullRequests(open);
            }
            if(fragments[1] == null) {
                fragments[1] = PullRequestCategory_.builder().pullRequests(merged).build();
            } else {
                fragments[1].setPullRequests(merged);
            }
            if(fragments[2] == null) {
                fragments[2] = PullRequestCategory_.builder().pullRequests(declined).build();
            } else {
                fragments[2].setPullRequests(declined);
            }
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
