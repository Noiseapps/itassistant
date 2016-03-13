package com.noiseapps.itassistant.fragment.stash;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.annimon.stream.Stream;
import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.adapters.stash.PullRequestActivityAdapter;
import com.noiseapps.itassistant.connector.StashConnector;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.stash.general.StashUser;
import com.noiseapps.itassistant.model.stash.projects.StashProject;
import com.noiseapps.itassistant.model.stash.pullrequests.MergeStatus;
import com.noiseapps.itassistant.model.stash.pullrequests.PullRequest;
import com.noiseapps.itassistant.model.stash.pullrequests.Veto;
import com.noiseapps.itassistant.model.stash.pullrequests.activities.PullRequestActivity;
import com.noiseapps.itassistant.utils.AuthenticatedPicasso;
import com.noiseapps.itassistant.utils.DividerItemDecoration;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import rx.Observable;

@EFragment(R.layout.layout_stash_pull_request_overview)
public class PullRequestOverviewFragment extends Fragment {

    @Bean
    StashConnector connector;

    @FragmentArg
    PullRequest pullRequest;

    @FragmentArg
    String repoSlug;

    @FragmentArg
    StashProject stashProject;

    @FragmentArg
    int pullRequestId;

    @FragmentArg
    BaseAccount account;

    @FragmentArg
    MergeStatus mergeStatus;

    @ViewById
    View fragmentRoot, stateButtons, fetchingPrActivities;

    @ViewById
    RecyclerView activityRecyclerView;

    @ViewById
    Button acceptButton, mergeButton, declineButton, reopenButton;

    @ViewById
    TextView pullRequestStatus, vetoesTextView;

    private MaterialDialog progressDialog;
    private PullRequestActivityAdapter adapter;

    @AfterViews
    void init() {
        updateStatusView();
    }

    private void getActivities() {
        fetchingPrActivities.setVisibility(View.VISIBLE);
        activityRecyclerView.setVisibility(View.GONE);
        fetchActivities();
    }

    @Background
    void fetchActivities() {
        final List<PullRequestActivity> activities = connector.getActivities(stashProject.getKey(), repoSlug, pullRequest.getId());
        setupListAdapter(activities);
    }

    @UiThread
    void setupListAdapter(List<PullRequestActivity> activities) {
        final Picasso authPicasso = AuthenticatedPicasso.getAuthPicasso(getActivity(), account);
        adapter = new PullRequestActivityAdapter(getActivity(), activities, authPicasso);
        activityRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        activityRecyclerView.setAdapter(adapter);

        fetchingPrActivities.setVisibility(View.GONE);
        activityRecyclerView.setVisibility(View.VISIBLE);
    }

    private void updateStatusView() {
        getActivities();
        if (pullRequest.getState().equalsIgnoreCase(PullRequest.STATUS_MERGED)) {
            createMergedState();
        } else if (pullRequest.getState().equalsIgnoreCase(PullRequest.STATUS_DECLINED)) {
            createPrDeclinedState();
        } else {
            createPrRegularState();
        }
    }

    private void createMergedState() {
        stateButtons.setVisibility(View.GONE);
        pullRequestStatus.setVisibility(View.VISIBLE);
        pullRequestStatus.setText(R.string.prIsMerged);

    }

    private void createPrRegularState() {
        acceptButton.setVisibility(View.VISIBLE);
        mergeButton.setVisibility(View.VISIBLE);
        declineButton.setVisibility(View.VISIBLE);
        reopenButton.setVisibility(View.GONE);
        pullRequestStatus.setVisibility(View.GONE);

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
        } else {
            acceptButton.setText(R.string.approve);
        }

        setVetoesView(vetoesTextView);

        mergeButton.setEnabled(mergeStatus.isCanMerge());
        setOnClickListeners(isApproved);
    }

    private void setOnClickListeners(boolean[] isApproved) {
        mergeButton.setOnClickListener(v -> onPrMergeClicked());
        acceptButton.setOnClickListener(v -> onPrAcceptClicked(isApproved));
        declineButton.setOnClickListener(v ->  {
            onPrDeclineClicked();
        });
    }

    private void onPrDeclineClicked() {
        final Observable<PullRequest> observable = connector.declinePullRequest(stashProject.getKey(), repoSlug, pullRequest.getId(), pullRequest.getVersion());
        subscribeForUpdate(observable);
    }

    private void onPrMergeClicked() {
        final Observable<PullRequest> observable = connector.mergePullRequest(stashProject.getKey(), repoSlug, pullRequest.getId(), pullRequest.getVersion());
        subscribeForUpdate(observable);
    }

    private void onPrAcceptClicked(boolean[] isApproved) {
        showProgress();
        final Observable<StashUser> observable;
        if(isApproved[0]) {
            observable = connector.unApprovePullRequest(stashProject.getKey(), repoSlug, pullRequest.getId(), pullRequest.getVersion());
        } else {
            observable = connector.approvePullRequest(stashProject.getKey(), repoSlug, pullRequest.getId(), pullRequest.getVersion());
        }
        observable.flatMap(stashUser1 -> connector.getPullRequest(stashProject.getKey(), repoSlug, pullRequest.getId())).
        flatMap(pullRequest1 -> {
            this.pullRequest = pullRequest1;
            return connector.checkPullRequestStatus(stashProject.getKey(), repoSlug, pullRequest.getId());
        }).subscribe(mergeStatusLocal -> {
            mergeStatus = mergeStatusLocal;
            hideProgress();
            updateStatusView();
        }, this::onUpdateError);
    }

    private void setVetoesView(TextView vetoes) {
        vetoes.setVisibility(View.GONE);
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
        mergeButton.setVisibility(View.GONE);
        acceptButton.setVisibility(View.GONE);
        declineButton.setVisibility(View.GONE);
        vetoesTextView.setVisibility(View.GONE);
        pullRequestStatus.setVisibility(View.VISIBLE);
        pullRequestStatus.setText(R.string.prIsDeclined);
        reopenButton.setVisibility(View.VISIBLE);
        reopenButton.setOnClickListener(v -> {
            showProgress();
            final Observable<PullRequest> observable = connector.reopenPullRequest(stashProject.getKey(), repoSlug, pullRequest.getId(), pullRequest.getVersion());
            observable.flatMap(pullRequest -> {
                PullRequestOverviewFragment.this.pullRequest = pullRequest;
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
            PullRequestOverviewFragment.this.pullRequest = pullRequest;
        }
        updateStatusView();
    }

    private void onUpdateError(Throwable throwable) {
        Logger.e(throwable, throwable.getMessage());
        hideProgress();
        Snackbar.make(fragmentRoot, R.string.failedToUpdatePr, Snackbar.LENGTH_LONG).show();
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
    
}
