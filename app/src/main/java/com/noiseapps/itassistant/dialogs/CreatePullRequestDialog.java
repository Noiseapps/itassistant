package com.noiseapps.itassistant.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.noiseapps.itassistant.BuildConfig;
import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.adapters.stash.ReviewersAdapter;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.stash.branches.BranchModel;
import com.noiseapps.itassistant.model.stash.general.StashUser;
import com.noiseapps.itassistant.model.stash.pullrequests.PullRequest;
import com.noiseapps.itassistant.utils.AuthenticatedPicasso;
import com.noiseapps.itassistant.utils.views.ReviewersAutocompleteView;
import com.tokenautocomplete.TokenCompleteTextView;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;


@EBean
public class CreatePullRequestDialog {

    @RootContext
    Context context;
    private List<BranchModel> availableBranches;
    private List<StashUser> users;
    private BaseAccount baseAccount;
    private CreatePrCallbacks callbacks;
    private AlertDialog alertDialog;

    public AlertDialog getAlertDialog() {
        return alertDialog;
    }

    public void onCreateError() {
        alertDialog.setCancelable(true);
        final View createPrForm = alertDialog.findViewById(R.id.createPrForm);
        final View creatingProgress = alertDialog.findViewById(R.id.creatingPr);
        createPrForm.setVisibility(View.VISIBLE);
        creatingProgress.setVisibility(View.GONE);
        Snackbar.make(createPrForm, context.getString(R.string.failedToAddPullRequest), Snackbar.LENGTH_LONG).show();
    }

    public interface CreatePrCallbacks {
        void onCreatePullRequest(PullRequest pullRequest, CreatePullRequestDialog createPullRequestDialog);
    }

    public void init(List<BranchModel> availableBranches, List<StashUser> users, BaseAccount baseAccount, CreatePrCallbacks callbacks) {
        this.availableBranches = availableBranches;
        this.users = users;
        this.baseAccount = baseAccount;
        this.callbacks = callbacks;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.dialog_create_pull_request);
        builder.setTitle(R.string.createPullRequest);
        builder.setPositiveButton(R.string.createPullRequest, (dialog1, which) -> {});
        builder.setNegativeButton(R.string.cancel, (dialog1, which) -> {});
        alertDialog = builder.create();
        alertDialog.setOnShowListener(dialog -> onCreatePrDialogShown(alertDialog));
        alertDialog.show();
}

    private void onCreatePrDialogShown(AlertDialog alertDialog) {
        SpinnerAdapter adapter = new ArrayAdapter<>(context,
                R.layout.item_spinner_textonly_black,
                R.id.title, availableBranches);
        final ReviewersAutocompleteView branchReviewers = (ReviewersAutocompleteView) alertDialog.findViewById(R.id.pullRequestReviewers);
        final Spinner sourceBranch = (Spinner) alertDialog.findViewById(R.id.sourceBranch);
        final Spinner targetBranch = (Spinner) alertDialog.findViewById(R.id.targetBranch);
        final TextView title = (TextView) alertDialog.findViewById(R.id.pullRequestsTitle);
        final View createPrForm = alertDialog.findViewById(R.id.createPrForm);
        final View creatingProgress = alertDialog.findViewById(R.id.creatingPr);
        if(BuildConfig.DEBUG) {
            title.setText("Testy ITAssistant [DO NOT MERGE]");
        }
        final List<StashUser> reviewers = new ArrayList<>();
        setupReviewerList(branchReviewers, title, reviewers);

        sourceBranch.setAdapter(adapter);
        targetBranch.setAdapter(adapter);
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> onCreateClick(branchReviewers, sourceBranch, targetBranch, title, createPrForm, creatingProgress, reviewers));
    }

    private void onCreateClick(ReviewersAutocompleteView branchReviewers, Spinner sourceBranch, Spinner targetBranch, TextView title, View createPrForm, View creatingProgress, List<StashUser> reviewers) {
        alertDialog.setCancelable(false);
        createPrForm.setVisibility(View.GONE);
        creatingProgress.setVisibility(View.VISIBLE);
        if(!BuildConfig.DEBUG && reviewers.isEmpty()) {
            branchReviewers.setError(context.getString(R.string.noReviewers));
        }

        final BranchModel sourceBranchSelected = (BranchModel) sourceBranch.getSelectedItem();
        final BranchModel targetBranchSelected = (BranchModel) targetBranch.getSelectedItem();
        final String prTitle = title.getText().toString();
        final PullRequest pullRequest = PullRequest.initialize(prTitle, sourceBranchSelected, targetBranchSelected, reviewers);
        callbacks.onCreatePullRequest(pullRequest, this);
    }

    private void setupReviewerList(ReviewersAutocompleteView branchReviewers, TextView title, final List<StashUser> reviewers) {
        branchReviewers.setSplitChar(' ');
        branchReviewers.allowCollapse(true);
        branchReviewers.setPicasso(AuthenticatedPicasso.getAuthPicasso(context, baseAccount));
        branchReviewers.setThreshold(1);
        branchReviewers.allowDuplicates(false);
        branchReviewers.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Delete);
        branchReviewers.setTokenListener(new TokenCompleteTextView.TokenListener<StashUser>() {
            @Override
            public void onTokenAdded(StashUser token) {
                reviewers.add(token);
                title.setError(null);
            }

            @Override
            public void onTokenRemoved(StashUser token) {
                reviewers.remove(token);
            }
        });
        branchReviewers.setAdapter(new ReviewersAdapter(context, users, baseAccount));
    }
}
