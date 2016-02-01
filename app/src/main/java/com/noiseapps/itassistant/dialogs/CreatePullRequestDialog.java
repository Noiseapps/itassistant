package com.noiseapps.itassistant.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.adapters.stash.ReviewersAdapter;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.stash.branches.BranchModel;
import com.noiseapps.itassistant.model.stash.general.StashUser;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;


@EBean
public class CreatePullRequestDialog {

    @RootContext
    Context context;
    private List<BranchModel> availableBranches;
    private List<StashUser> users;
    private BaseAccount baseAccount;
    private CreatePrCallbacks callbacks;

    public interface CreatePrCallbacks {
        void onCreatePullRequest();
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
        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(dialog -> onCreatePrDialogShown(alertDialog));
        alertDialog.show();
}

    private void onCreatePrDialogShown(AlertDialog alertDialog) {

        SpinnerAdapter adapter = new ArrayAdapter<>(context,
                R.layout.item_spinner_textonly_black,
                R.id.title, availableBranches);
        final Spinner sourceBranch = (Spinner) alertDialog.findViewById(R.id.sourceBranch);
        final Spinner targetBranch = (Spinner) alertDialog.findViewById(R.id.targetBranch);

        final MultiAutoCompleteTextView branchReviewers = (MultiAutoCompleteTextView) alertDialog.findViewById(R.id.branchReviewers);
        branchReviewers.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        branchReviewers.setThreshold(1);
        final List<String> userList = Stream.of(users).map(StashUser::getName).collect(Collectors.toList());
        branchReviewers.setAdapter(new ReviewersAdapter(context, users, baseAccount));


        sourceBranch.setAdapter(adapter);
        targetBranch.setAdapter(adapter);
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
            final BranchModel sourceBranchSelected = (BranchModel) sourceBranch.getSelectedItem();
            final BranchModel targetBranchSelected = (BranchModel) targetBranch.getSelectedItem();
            String reviewers = branchReviewers.getText().toString();
            // TODO: 01.02.2016 create new branch model
            callbacks.onCreatePullRequest();
        });
    }
}
