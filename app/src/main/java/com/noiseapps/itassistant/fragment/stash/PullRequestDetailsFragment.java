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

            items[1] = PullRequestDiffFragment_.builder().build();
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


    private class StashDetailsPagerAdapter extends PagerAdapter {

        private final String[] tabTitles;
        private final LayoutInflater inflater;
        private MaterialDialog progressDialog;
        private View statusView;

        public StashDetailsPagerAdapter() {
            tabTitles = getResources().getStringArray(R.array.prTabs);
            inflater = LayoutInflater.from(getActivity());
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final View view;
            if (position == 0) {
                view = createStatusView(container);
            } else {
                view = createDiffView(container);
            }
            container.addView(view);
            return view;
        }

        private View createStatusView(ViewGroup container) {
            statusView = inflater.inflate(R.layout.layout_stash_pull_request_overview, container, false);
            updateStatusView();
            return statusView;
        }

        private void updateStatusView() {
            if (pullRequest.getState().equalsIgnoreCase(PullRequest.STATUS_MERGED)) {
                createMergedState();
            } else if (pullRequest.getState().equalsIgnoreCase(PullRequest.STATUS_DECLINED)) {
                createPrDeclinedState();
            } else {
                createPrRegularState();
            }
        }

        private void createMergedState() {
            statusView.findViewById(R.id.stateButtons).setVisibility(View.GONE);
            final TextView statusTextView = (TextView) statusView.findViewById(R.id.pullRequestStatus);
            statusTextView.setVisibility(View.VISIBLE);
            statusTextView.setText(R.string.prIsMerged);

        }

        private void createPrRegularState() {
            final Button acceptButton = (Button) statusView.findViewById(R.id.acceptButton);
            final Button mergeButton = (Button) statusView.findViewById(R.id.mergeButton);
            final Button declineButton = (Button) statusView.findViewById(R.id.declineButton);
            acceptButton.setVisibility(View.VISIBLE);
            mergeButton.setVisibility(View.VISIBLE);
            declineButton.setVisibility(View.VISIBLE);
            statusView.findViewById(R.id.reopenButton).setVisibility(View.GONE);
            statusView.findViewById(R.id.pullRequestStatus).setVisibility(View.GONE);

            final TextView vetoes = (TextView) statusView.findViewById(R.id.vetoesTextView);

            final boolean[] isApproved = {false};

            if(pullRequest.getAuthor().getUser().getName().equalsIgnoreCase(account.getUsername())) {
                acceptButton.setEnabled(false);
            }

            Stream.of(pullRequest.getReviewers()).
                    filter(requestMember -> requestMember.getUser().getName().equalsIgnoreCase(account.getUsername())).
                    findFirst().
                    ifPresent(value -> isApproved[0] = value.isApproved());

            if(isApproved[0]) {
                acceptButton.setText(R.string.unapprove);
            }

            setVetoesView(vetoes);

            mergeButton.setEnabled(mergeStatus.isCanMerge());
            mergeButton.setOnClickListener(v ->  {
                final Observable<PullRequest> observable = connector.mergePullRequest(stashProject.getKey(), repoSlug, pullRequest.getId(), pullRequest.getVersion());
                subscribeForUpdate(observable);
            });
            acceptButton.setOnClickListener(v ->  {
                final Observable<PullRequest> observable;
                if(isApproved[0]) {
                    observable = connector.unApprovePullRequest(stashProject.getKey(), repoSlug, pullRequest.getId(), pullRequest.getVersion());
                } else {
                    observable = connector.approvePullRequest(stashProject.getKey(), repoSlug, pullRequest.getId(), pullRequest.getVersion());
                }
//                subscribeForUpdate(observable);

            });
            declineButton.setOnClickListener(v ->  {
                final Observable<PullRequest> observable = connector.declinePullRequest(stashProject.getKey(), repoSlug, pullRequest.getId(), pullRequest.getVersion());
                subscribeForUpdate(observable);
            });
        }

        private void setVetoesView(TextView vetoes) {
            if(!mergeStatus.isCanMerge()) {
                final StringBuilder sb = new StringBuilder();
                for (Veto veto : mergeStatus.getVetoes()) {
                    sb.append(veto.getDetailedMessage()).append('\n');
                }
                vetoes.setText(sb.toString());
                vetoes.setVisibility(View.VISIBLE);
            }
        }

        private void createPrDeclinedState() {
            statusView.findViewById(R.id.mergeButton).setVisibility(View.GONE);
            statusView.findViewById(R.id.acceptButton).setVisibility(View.GONE);
            statusView.findViewById(R.id.declineButton).setVisibility(View.GONE);
            final View reopenButton = statusView.findViewById(R.id.reopenButton);
            statusView.findViewById(R.id.vetoesTextView).setVisibility(View.GONE);
            final TextView statusTextView = (TextView) statusView.findViewById(R.id.pullRequestStatus);
            statusTextView.setVisibility(View.VISIBLE);
            statusTextView.setText(R.string.prIsDeclined);
            reopenButton.setVisibility(View.VISIBLE);
            reopenButton.setOnClickListener(v -> {
                showProgress();
                final Observable<PullRequest> observable = connector.reopenPullRequest(stashProject.getKey(), repoSlug, pullRequest.getId(), pullRequest.getVersion());
                observable.flatMap(pullRequest -> {
                    PullRequestDetailsFragment.this.pullRequest = pullRequest;
                    return connector.checkPullRequestStatus(stashProject.getKey(), repoSlug, pullRequest.getId());
                }).subscribe(mergeStatusLocal -> {
                    mergeStatus = mergeStatusLocal;
                    hideProgress();
                    updateStatusView();
                }, this::onUpdateError);
            });
        }

        public void subscribeForUpdate(Observable<PullRequest> observable) {
            showProgress();
            observable.subscribe(this::onUpdated, this::onUpdateError);
        }

        private void onUpdated(PullRequest pullRequest) {
            hideProgress();
            if(pullRequest != null) {
                PullRequestDetailsFragment.this.pullRequest = pullRequest;
            }
            updateStatusView();
        }

        private void onUpdateError(Throwable throwable) {
            Logger.e(throwable, throwable.getMessage());
            hideProgress();
            Snackbar.make(viewPager, R.string.failedToUpdatePr, Snackbar.LENGTH_LONG).show();
        }

        private void hideProgress() {
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }

        private void showProgress() {
            progressDialog = new MaterialDialog.Builder(getActivity()).
                    progress(true, 0).
                    content(R.string.updating).
                    cancelable(false).show();
        }

        private View createDiffView(ViewGroup container) {
            return new TextView(getActivity());
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
