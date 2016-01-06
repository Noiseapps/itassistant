package com.noiseapps.itassistant.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.connector.StashConnector;
import com.noiseapps.itassistant.model.stash.projects.BranchModel;
import com.noiseapps.itassistant.model.stash.projects.NewBranchModel;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.List;

@EBean
public class CreateBranchDialog {

    private final Context context;
    private String projectKey;
    private String repoSlug;
    private OnBranchCreatedCallback callback;
    @Bean
    StashConnector stashConnector;
    private List<BranchModel> branches;

    public interface OnBranchCreatedCallback {
        void branchCreated(BranchModel branchModel);
    }

    public void init(String projectKey, String repoSlug, List<BranchModel> branches, OnBranchCreatedCallback callback) {
        this.projectKey = projectKey;
        this.repoSlug = repoSlug;
        this.branches = branches;
        this.callback = callback;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.dialog_create_branch);
        builder.setTitle(R.string.createBranch);
        builder.setPositiveButton(R.string.createBranch, (dialog1, which) -> {});
        builder.setNegativeButton(R.string.cancel, (dialog1, which) -> {});
        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(dialog -> onDialogShown(alertDialog));
        alertDialog.show();
    }

    public CreateBranchDialog(Context context) {
        this.context = context;
    }


    private void onDialogShown(AlertDialog alertDialog) {
        SpinnerAdapter adapter = new ArrayAdapter<>(context,
                R.layout.item_spinner_textonly_black,
                R.id.title, branches);
        final Spinner branchesSpinner = (Spinner) alertDialog.findViewById(R.id.branchesSpinner);
        final EditText branchNameEdit = (EditText) alertDialog.findViewById(R.id.branchName);
        final View creatingBranch = alertDialog.findViewById(R.id.creatingBranch);
        final View rootView = alertDialog.findViewById(R.id.mainViewRoot);
        branchesSpinner.setAdapter(adapter);
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
            rootView.setVisibility(View.GONE);
            creatingBranch.setVisibility(View.VISIBLE);
            final BranchModel selectedItem = (BranchModel) branchesSpinner.getSelectedItem();
            final String branchName = branchNameEdit.getText().toString().trim();
            createNewBranch(alertDialog, selectedItem, branchName);
        });
    }

    private void createNewBranch(AlertDialog dialog, BranchModel selectedItem, String branchName) {
        final NewBranchModel newBranchModel = new NewBranchModel(branchName, selectedItem.getId());
        stashConnector.createBranch(projectKey, repoSlug, newBranchModel).
                subscribe(branchModel -> {
                    onBranchCreated(branchModel, dialog);
                }, throwable -> {
                    onCreateFailed(throwable, dialog);
                });
    }

    private void onCreateFailed(Throwable throwable, AlertDialog dialog) {
        Logger.e(throwable, throwable.getMessage());
        dialog.findViewById(R.id.mainViewRoot).setVisibility(View.VISIBLE);
        dialog.findViewById(R.id.creatingBranch).setVisibility(View.GONE);
        Snackbar.make(dialog.findViewById(R.id.branchName), R.string.createBranchFailed, Snackbar.LENGTH_LONG).show();
    }

    private void onBranchCreated(BranchModel branchModel, DialogInterface dialog) {
        callback.branchCreated(branchModel);
        dialog.dismiss();
    }
}
