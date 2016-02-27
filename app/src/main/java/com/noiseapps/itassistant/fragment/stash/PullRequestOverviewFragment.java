package com.noiseapps.itassistant.fragment.stash;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.annimon.stream.Stream;
import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.api.StashAPI;
import com.noiseapps.itassistant.connector.StashConnector;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.stash.projects.StashProject;
import com.noiseapps.itassistant.model.stash.pullrequests.MergeStatus;
import com.noiseapps.itassistant.model.stash.pullrequests.PullRequest;
import com.noiseapps.itassistant.model.stash.pullrequests.Veto;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

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
    View fragmentRoot, stateButtons;

    @ViewById
    Button acceptButton, mergeButton, declineButton, reopenButton;

    @ViewById
    TextView pullRequestStatus, vetoesTextView;

    private MaterialDialog progressDialog;

    @AfterViews
    void init() {
        // TODO: 26.02.2016 get activities
        updateStatusView();
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
        }

        setVetoesView(vetoesTextView);

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
//            subscribeForUpdate(observable);

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
