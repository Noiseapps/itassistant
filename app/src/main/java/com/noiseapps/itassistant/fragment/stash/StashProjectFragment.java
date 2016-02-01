package com.noiseapps.itassistant.fragment.stash;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.connector.StashConnector;
import com.noiseapps.itassistant.connector.StashConnector_;
import com.noiseapps.itassistant.dialogs.CreateBranchDialog;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.stash.branches.BranchModel;
import com.noiseapps.itassistant.model.stash.general.CloneLink;
import com.noiseapps.itassistant.model.stash.general.ProjectRepos;
import com.noiseapps.itassistant.model.stash.projects.StashProject;
import com.noiseapps.itassistant.model.stash.general.StashRepoMeta;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EFragment(R.layout.fragment_stash_project)
public class StashProjectFragment extends Fragment {

    @ViewById
    LinearLayout fetchingDataProgress, noProjectData, cloneLinks;
    @ViewById
    FrameLayout rootView;
    @ViewById
    NestedScrollView stashMenuRoot;

    @SystemService
    ClipboardManager clipboardManager;


    StashProject stashProject;
    private BaseAccount baseAccount;

    @Bean
    StashConnector stashConnector;

    @Bean
    CreateBranchDialog createBranchDialog;
    private StashRepoMeta currentRepo;
    private List<BranchModel> branches;
    private StashMenuCallbacks menuCallbacks;
    private ProgressDialog fetchingBranches;


    public interface StashMenuCallbacks {
        void onShowBranchesList(@NonNull StashProject stashProject, @NonNull String slug);

        void onShowCommitsList(StashProject stashProject, String slug);

        void onShowPullRequestList(StashProject stashProject, String slug, BaseAccount baseAccount);
    }

    @AfterViews
    void init() {
        menuCallbacks = ((StashMenuCallbacks) getActivity());
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
                    loadProjectDetails(item);
                    branches = null;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            supportActionBar.setDisplayShowCustomEnabled(true);
        }
    }

    @Background
    void getBranches() {
        final List<BranchModel> branches = stashConnector.getBranches(stashProject.getKey(), currentRepo.getSlug());
        onBranchesDownloaded(branches);
    }

    @UiThread
    void onBranchesDownloaded(List<BranchModel> branches) {
        if(fetchingBranches.isShowing()) {
            fetchingBranches.dismiss();
            createBranchDialog.init(stashProject.getKey(), currentRepo.getSlug(), branches, this::showSuccessMessage);
        }
    }

    @UiThread
    void loadProjectDetails(StashRepoMeta item) {
        currentRepo = item;
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
        cloneLinks.findViewById(R.id.copyLink).setOnClickListener(v -> onCopyCloneLinkClicked(cloneLinkName, cloneLinkHref));
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
        if (branches == null) {
            fetchingBranches = new ProgressDialog(getActivity());
            fetchingBranches.setMessage(getString(R.string.fetchingBranches));
            fetchingBranches.setIndeterminate(true);
            fetchingBranches.setCancelable(true);
            fetchingBranches.setOnCancelListener(DialogInterface::dismiss);
            fetchingBranches.show();
            getBranches();
        } else {
            onShowBranches();
        }
    }

    private void showSuccessMessage(BranchModel branchModel) {
        Snackbar.make(rootView, getString(R.string.branchCreated, branchModel.getDisplayId()), Snackbar.LENGTH_LONG).show();
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

//    private void onCreatePrDialogShown(AlertDialog alertDialog) {
//        SpinnerAdapter adapter = new ArrayAdapter<>(getActivity(),
//                R.layout.item_spinner_textonly_black,
//                R.id.title, branches);
//        final Spinner sourceBranch = (Spinner) alertDialog.findViewById(R.id.sourceBranch);
//        final Spinner targetBranch = (Spinner) alertDialog.findViewById(R.id.targetBranch);
//        final View creatingBranch = alertDialog.findViewById(R.id.creatingBranch);
//        sourceBranch.setAdapter(adapter);
//        targetBranch.setAdapter(adapter);
//        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
//            creatingBranch.setVisibility(View.VISIBLE);
//            final BranchModel selectedItem = (BranchModel) branchesSpinner.getSelectedItem();
//            final String branchName = branchNameEdit.getText().toString().trim();
//            createNewBranch(alertDialog, selectedItem, branchName);
//        });
//    }

//    @Click(R.id.fork)
//    void onForkRepo() {
//        // todo show fork dialog
//    }
//
    @Click(R.id.pullRequests)
    void onShowPullRequests() {
        menuCallbacks.onShowPullRequestList(stashProject, currentRepo.getSlug(), baseAccount);
    }

    @Click(R.id.branches)
    void onShowBranches() {
        menuCallbacks.onShowBranchesList(stashProject, currentRepo.getSlug());
    }

    @Click(R.id.commits)
    void onShowCommits() {
        menuCallbacks.onShowCommitsList(stashProject, currentRepo.getSlug());
    }

    private void showProgress() {
        fetchingDataProgress.setVisibility(View.VISIBLE);
    }

}
