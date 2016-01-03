package com.noiseapps.itassistant.fragment.stash;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.connector.StashConnector;
import com.noiseapps.itassistant.connector.StashConnector_;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.atlassian.PagedApiModel;
import com.noiseapps.itassistant.model.stash.projects.BranchModel;
import com.noiseapps.itassistant.model.stash.projects.CloneLink;
import com.noiseapps.itassistant.model.stash.projects.NewBranchModel;
import com.noiseapps.itassistant.model.stash.projects.ProjectRepos;
import com.noiseapps.itassistant.model.stash.projects.StashProject;
import com.noiseapps.itassistant.model.stash.projects.StashRepoMeta;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EFragment(R.layout.fragment_stash_project)
public class StashProjectFragment extends Fragment {

    @ViewById
    LinearLayout fetchingDataProgress, noProjectData, cloneLinks;
    @ViewById
    FrameLayout rootView;;
    @ViewById
    NestedScrollView stashMenuRoot;

    @SystemService
    ClipboardManager clipboardManager;


    StashProject stashProject;
    private BaseAccount baseAccount;

    @Bean
    StashConnector stashConnector;
    private StashRepoMeta currentRepo;
    private List<BranchModel> branches;

    @AfterViews
    void init() {
        configureToolbar(true);
    }

    private void configureToolbar(boolean showCustomView) {
        final ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setCustomView(R.layout.layout_toolbar_spinner);
            supportActionBar.setDisplayShowCustomEnabled(showCustomView);
            supportActionBar.setDisplayShowTitleEnabled(!showCustomView);
        }
    }

    @Override
    public void onDetach() {
        configureToolbar(false);
        super.onDetach();
    }

    public void setProject(StashProject jiraProject, BaseAccount baseAccount) {
        stashMenuRoot.setVisibility(View.GONE);
        showProgress();
        stashProject = jiraProject;
        this.baseAccount = baseAccount;
        if (stashConnector == null) {
            stashConnector = StashConnector_.getInstance_(getContext());
        }
        stashConnector.getProjectRepos(stashProject.getKey()).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(this::onReposDownloaded, this::onDownloadFailed);
        Logger.d(String.valueOf(stashProject));
    }


    private void onDownloadFailed(Throwable throwable) {
        showError();
    }

    private void showError() {
        hideProgress();
        Snackbar.make(rootView, R.string.failedToFetchDetails, Snackbar.LENGTH_LONG).show();
    }

    private void hideProgress() {
        fetchingDataProgress.setVisibility(View.GONE);
    }

    private void onReposDownloaded(ProjectRepos repos) {
        final ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setCustomView(R.layout.layout_toolbar_spinner);
            final Spinner toolbarSpinner = (Spinner) supportActionBar.getCustomView().findViewById(R.id.spinner);
            final ArrayAdapter<StashRepoMeta> adapter = new ArrayAdapter<>(getActivity(), R.layout.item_spinner_textonly, R.id.title, repos.getRepos());
            toolbarSpinner.setAdapter(adapter);
            toolbarSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    final StashRepoMeta item = adapter.getItem(position);
                    getBranches(item);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            supportActionBar.setDisplayShowCustomEnabled(true);
        }
    }

    @Background
    void getBranches(StashRepoMeta item) {
        final List<BranchModel> branches = stashConnector.getBranches(stashProject.getKey(), item.getSlug());
        loadProjectDetails(item, branches);
    }

    @UiThread
    void loadProjectDetails(StashRepoMeta item, List<BranchModel> branches) {
        currentRepo = item;
        this.branches = branches;
        hideProgress();
        stashMenuRoot.setVisibility(View.VISIBLE);
        configureCloneLinks(item);
    }

    private void configureCloneLinks(StashRepoMeta item) {
        cloneLinks.removeAllViews();
        final LayoutInflater inflater = LayoutInflater.from(getActivity());
        for (CloneLink cloneLink : item.getLinks().getCloneLinks()) {
            addSingleCloneLink(inflater, cloneLink);
        }
    }

    private void addSingleCloneLink(LayoutInflater inflater, CloneLink cloneLink) {
        final View cloneLinks = inflater.inflate(R.layout.layout_clone_link, this.cloneLinks, false);
        final String cloneLinkName = cloneLink.getName().toUpperCase();
        final String cloneLinkHref = cloneLink.getHref();

        ((TextView) cloneLinks.findViewById(R.id.linkType)).setText(cloneLinkName);
        ((TextView) cloneLinks.findViewById(R.id.linkAddress)).setText(cloneLinkHref);
        cloneLinks.findViewById(R.id.copyLink).setOnClickListener(v -> {
            onCopyCloneLinkClicked(cloneLinkName, cloneLinkHref);
        });
        this.cloneLinks.addView(cloneLinks);
    }

    private void onCopyCloneLinkClicked(String cloneLinkName, String cloneLinkHref) {
        final Uri uri = Uri.parse(cloneLinkHref);
        final ClipData clipData = ClipData.newRawUri(cloneLinkName, uri);
        clipboardManager.setPrimaryClip(clipData);
        Snackbar.make(rootView, R.string.linkCopied, Snackbar.LENGTH_LONG).show();
    }

    @Click(R.id.createBranch)
    void onCreateBranch() {
        if(branches == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.dialog_create_branch);
        builder.setTitle(R.string.createBranch);
        builder.setPositiveButton(R.string.createBranch, (dialog1, which) -> {});
        builder.setNegativeButton(R.string.cancel, (dialog1, which) -> {});
        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(dialog -> {
            onDialogShown(alertDialog);
        });
        alertDialog.show();
    }

    private void onDialogShown(AlertDialog alertDialog) {
        SpinnerAdapter adapter = new ArrayAdapter<>(getActivity(),
                R.layout.item_spinner_textonly_black,
                R.id.title, branches);
        final Spinner branchesSpinner = (Spinner) alertDialog.findViewById(R.id.branchesSpinner);
        final EditText branchNameEdit = (EditText) alertDialog.findViewById(R.id.branchName);
        final View creatingBranch = alertDialog.findViewById(R.id.creatingBranch);
        branchesSpinner.setAdapter(adapter);
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
            creatingBranch.setVisibility(View.VISIBLE);
            final BranchModel selectedItem = (BranchModel) branchesSpinner.getSelectedItem();
            final String branchName = branchNameEdit.getText().toString().trim();
            createNewBranch(alertDialog, selectedItem, branchName);
        });
    }

    private void createNewBranch(AlertDialog dialog, BranchModel selectedItem, String branchName) {
        final NewBranchModel newBranchModel = new NewBranchModel(branchName, selectedItem.getId());
        stashConnector.createBranch(stashProject.getKey(), currentRepo.getSlug(), newBranchModel).
                subscribe(branchModel -> {
                    onBranchCreated(branchModel, dialog);
                }, throwable -> {
                    onCreateFailed(throwable, dialog);
                });
    }

    private void onCreateFailed(Throwable throwable, AlertDialog dialog) {
        Logger.e(throwable, throwable.getMessage());
        dialog.findViewById(R.id.creatingBranch).setVisibility(View.GONE);
        Snackbar.make(dialog.findViewById(R.id.branchName), R.string.createBranchFailed, Snackbar.LENGTH_LONG).show();
    }

    private void onBranchCreated(BranchModel branchModel, DialogInterface dialog) {
        Snackbar.make(rootView, getString(R.string.branchCreated, branchModel.getDisplayId()), Snackbar.LENGTH_LONG).show();
        dialog.dismiss();
    }

    @Click(R.id.createPR)
    void onCreatePullRequest() {
//        if(branches == null) {
//            return;
//        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setView(R.layout.dialog_create_pull_request);
//        builder.setTitle(R.string.createPullRequest);
//        builder.setPositiveButton(R.string.createPullRequest, (dialog1, which) -> {});
//        builder.setNegativeButton(R.string.cancel, (dialog1, which) -> {});
//        final AlertDialog alertDialog = builder.create();
//        alertDialog.setOnShowListener(dialog -> {
//            onCreatePrDialogShown(alertDialog);
//        });
//        alertDialog.show();
    }

    private void onCreatePrDialogShown(AlertDialog alertDialog) {
        SpinnerAdapter adapter = new ArrayAdapter<>(getActivity(),
                R.layout.item_spinner_textonly_black,
                R.id.title, branches);
        final Spinner sourceBranch = (Spinner) alertDialog.findViewById(R.id.sourceBranch);
        final Spinner targetBranch = (Spinner) alertDialog.findViewById(R.id.targetBranch);
        final View creatingBranch = alertDialog.findViewById(R.id.creatingBranch);
        sourceBranch.setAdapter(adapter);
        targetBranch.setAdapter(adapter);
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
            creatingBranch.setVisibility(View.VISIBLE);
            final BranchModel selectedItem = (BranchModel) branchesSpinner.getSelectedItem();
            final String branchName = branchNameEdit.getText().toString().trim();
            createNewBranch(alertDialog, selectedItem, branchName);
        });
    }

    @Click(R.id.fork)
    void onForkRepo() {
        // todo show fork dialog
    }

    private void showProgress() {
        fetchingDataProgress.setVisibility(View.VISIBLE);
    }

}
