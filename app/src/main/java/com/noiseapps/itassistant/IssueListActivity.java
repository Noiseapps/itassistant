package com.noiseapps.itassistant;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.afollestad.materialdialogs.MaterialDialog;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.noiseapps.itassistant.adapters.NavigationMenuAdapter;
import com.noiseapps.itassistant.connector.JiraConnector;
import com.noiseapps.itassistant.connector.StashConnector;
import com.noiseapps.itassistant.database.dao.AccountsDao;
import com.noiseapps.itassistant.fragment.IssueDetailFragment;
import com.noiseapps.itassistant.fragment.IssueDetailFragment_;
import com.noiseapps.itassistant.fragment.IssueListFragment;
import com.noiseapps.itassistant.fragment.IssueListFragment_;
import com.noiseapps.itassistant.fragment.NewIssueFragment;
import com.noiseapps.itassistant.fragment.NewIssueFragment_;
import com.noiseapps.itassistant.fragment.stash.BranchListFragment;
import com.noiseapps.itassistant.fragment.stash.BranchListFragment_;
import com.noiseapps.itassistant.fragment.stash.CommitListFragment;
import com.noiseapps.itassistant.fragment.stash.CommitListFragment_;
import com.noiseapps.itassistant.fragment.stash.PullRequestListFragment;
import com.noiseapps.itassistant.fragment.stash.PullRequestListFragment_;
import com.noiseapps.itassistant.fragment.stash.StashProjectFragment;
import com.noiseapps.itassistant.fragment.stash.StashProjectFragment_;
import com.noiseapps.itassistant.model.NavigationModel;
import com.noiseapps.itassistant.model.account.AccountTypes;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.atlassian.AbstractBaseProject;
import com.noiseapps.itassistant.model.jira.issues.Issue;
import com.noiseapps.itassistant.model.jira.projects.JiraProject;
import com.noiseapps.itassistant.model.stash.projects.StashProject;
import com.noiseapps.itassistant.model.stash.projects.UserProjects;
import com.noiseapps.itassistant.utils.AuthenticatedPicasso;
import com.noiseapps.itassistant.utils.Consts;
import com.noiseapps.itassistant.utils.DividerItemDecoration;
import com.noiseapps.itassistant.utils.events.EventBusAction;
import com.noiseapps.itassistant.utils.events.OpenDrawerEvent;
import com.orhanobut.logger.Logger;
import com.suredigit.inappfeedback.FeedbackDialog;
import com.suredigit.inappfeedback.FeedbackSettings;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import jonathanfinerty.once.Once;

import static com.noiseapps.itassistant.AnalyticsTrackers.CATEGORY_APP;
import static com.noiseapps.itassistant.AnalyticsTrackers.CATEGORY_ISSUES;
import static com.noiseapps.itassistant.AnalyticsTrackers.CATEGORY_MENU;
import static com.noiseapps.itassistant.AnalyticsTrackers.SCREEN_ISSUE_LIST;

@EActivity(R.layout.activity_issue_app_bar)
public class IssueListActivity extends AppCompatActivity
        implements IssueListFragment.Callbacks,
        NewIssueFragment.NewIssueCallbacks,
        IssueDetailFragment.IssueDetailCallbacks,
        StashProjectFragment.StashMenuCallbacks {

    public static final int ACCOUNTS_REQUEST = 633;
    public static final int NEW_ISSUE_REQUEST = 5135;
    public static final int DELAY_MILLIS = 2000;
    @ViewById
    Toolbar toolbar;
    @ViewById
    CoordinatorLayout coordinatorLayout;
    @ViewById(R.id.recyclerView)
    RecyclerView navigationRecycler;
    @ViewById
    View nothingSelectedInfo;
    @ViewById
    DrawerLayout drawerLayout;

    private IssueListFragment listFragment;
    private StashProjectFragment stashFragment;
    @Bean
    AccountsDao accountsDao;
    @Bean
    JiraConnector jiraConnector;
    @Bean
    StashConnector stashConnector;
    ArrayList<NavigationModel> navigationModels;
    @Bean
    AnalyticsTrackers tracker;
    private boolean mTwoPane;
    private MaterialDialog progressDialog;
    private ArrayList<Issue> myIssues;
    private FeedbackDialog feedbackDialog;
    private boolean doubleClicked;
    private Handler handler;

    @Override
    public void onItemSelected(Issue issue) {
        tracker.sendEvent(SCREEN_ISSUE_LIST, CATEGORY_ISSUES, "viewDetails");
        if (mTwoPane) {
            nothingSelectedInfo.setVisibility(View.GONE);
            final IssueDetailFragment fragment = IssueDetailFragment_.builder().issue(issue).build();
            setDetailsFragment(fragment, null);
        } else {
            IssueDetailActivity_.intent(this).issue(issue).start();
        }
    }

    private void setDetailsFragment(Fragment fragment, @Nullable String backstackName) {
        getSupportFragmentManager().beginTransaction().addToBackStack(backstackName)
                .replace(R.id.issue_detail_container, fragment)
                .commit();
    }

    @Override
    public void onAddNewIssue(JiraProject jiraProject) {
        tracker.sendEvent(SCREEN_ISSUE_LIST, CATEGORY_ISSUES, "addNewIssue");
        final String key = jiraProject.getKey();
        if (mTwoPane) {
            nothingSelectedInfo.setVisibility(View.GONE);
            final NewIssueFragment fragment = NewIssueFragment_.builder().projectKey(key).build();
            setDetailsFragment(fragment, "CREATE");
        } else {
            NewIssueActivity_.intent(this).projectKey(key).startForResult(NEW_ISSUE_REQUEST);
        }
    }

    @Override
    public void onEditIssue(Issue issue) {
        tracker.sendEvent(SCREEN_ISSUE_LIST, CATEGORY_ISSUES, "editIssue");
        final String key = issue.getFields().getProject().getKey();
        if (mTwoPane) {
            nothingSelectedInfo.setVisibility(View.GONE);
            final NewIssueFragment fragment = NewIssueFragment_.builder().projectKey(key).issue(issue).build();
            setDetailsFragment(fragment, "EDIT");
        } else {
            NewIssueActivity_.intent(this).projectKey(key).issue(issue).startForResult(NEW_ISSUE_REQUEST);
        }
    }

    @EventBusAction
    public void onEvent(@Nullable OpenDrawerEvent event) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onIssueCreated(Issue issue) {
        listFragment.reload();
        if (mTwoPane) {
            onItemSelected(issue);
        }
    }

    @AfterViews
    void init() {
        listFragment = IssueListFragment_.builder().build();
        stashFragment = StashProjectFragment_.builder().build();
        handler = new Handler();
        setTablet();
        if (accountsDao.getAll().isEmpty()) {
            showNoAccountsDialog();
        } else {
            if (navigationModels == null) {
                downloadData();
            } else {
                initNavigation(navigationModels);
            }
        }
        initToolbar();
        isTwoPane();

        feedbackDialog = new FeedbackDialog(this, "AF-EBD0453F2EC0-CF");
        AnalyticsTrackers_.getInstance_(this).sendScreenVisit(SCREEN_ISSUE_LIST);
    }

    private void setTablet() {
        if (getResources().getBoolean(R.bool.tabletSize)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
    }

    private void showNoAccountsDialog() {
        AlertDialogWrapper.Builder builder = new AlertDialogWrapper.Builder(this);
        builder.setTitle(R.string.noAccounts);
        builder.setMessage(R.string.noAccountsMsg);
        builder.setPositiveButton(R.string.yes, (dialog, which) -> {
            startAccountsActivity(true);
        });
        builder.setNegativeButton(R.string.quit, (dialog, which) -> {
            finish();
        });
        builder.show();
    }

    private void startAccountsActivity(boolean showForm) {
        AccountsActivity_.intent(this).showAccountForm(showForm).startForResult(ACCOUNTS_REQUEST);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        final boolean isSearchOpen = listFragment.isSearchViewOpen();
        if (isSearchOpen) {
            listFragment.closeSearchView();
        } else if (!doubleClicked) {
            Snackbar.make(coordinatorLayout, R.string.tapAgainToExit, Snackbar.LENGTH_LONG).show();
            doubleClicked = true;
            handler.postDelayed(() -> doubleClicked = false, DELAY_MILLIS);
        } else {
            super.onBackPressed();
        }
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.app_name));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        final ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    @Background
    void downloadData() {
        showProgress();
        navigationModels = new ArrayList<>();
        myIssues = new ArrayList<>();
        int failedAccounts = 0;
        for (final BaseAccount baseAccount : accountsDao.getAll()) {
            if(baseAccount.getAccountType() == AccountTypes.ACC_JIRA) {
                failedAccounts = fetchJiraAccountInfo(failedAccounts, baseAccount);
            } else {
                failedAccounts = fetchStashAccountInfo(failedAccounts, baseAccount);
            }
        }
        hideProgress();
        showInfoAboutFailedAccounts(failedAccounts);
        initMyIssues(myIssues);
        initNavigation(navigationModels);
    }

    private int fetchStashAccountInfo(int failedAccounts, BaseAccount baseAccount) {
        try {
            stashConnector.setCurrentConfig(baseAccount);
            AuthenticatedPicasso.setConfig(this, baseAccount);
            final UserProjects projects = stashConnector.getProjects();
            if (projects != null) {
                AbstractBaseProject[] baseProjects = new AbstractBaseProject[projects.getStashProjects().size()];
                baseProjects = projects.getStashProjects().toArray(baseProjects);
                navigationModels.add(new NavigationModel(baseAccount, baseProjects));
            } else {
                failedAccounts++;
            }
        } catch (Exception e) {
            Logger.e(e, e.getMessage());
        }
        return failedAccounts;
    }

    private int fetchJiraAccountInfo(int failedAccounts, BaseAccount baseAccount) {
        try {
            jiraConnector.setCurrentConfig(baseAccount);
            AuthenticatedPicasso.setConfig(this, baseAccount);
            final List<JiraProject> jiraProjects = jiraConnector.getUserProjects();
            if (jiraProjects != null) {
                AbstractBaseProject[] baseProjects = new AbstractBaseProject[jiraProjects.size()];
                baseProjects = jiraProjects.toArray(baseProjects);
                navigationModels.add(new NavigationModel(baseAccount, baseProjects));
            } else {
                failedAccounts++;
            }
            final List<Issue> myProjectIssues = jiraConnector.getAssignedToMe();
            myIssues.addAll(myProjectIssues);
        } catch (Exception e) {
            Logger.e(e, e.getMessage());
        }
        return failedAccounts;
    }

    @UiThread
    void initMyIssues(List<Issue> myIssues) {
        getSupportFragmentManager().beginTransaction().replace(R.id.listContainer, listFragment).commitAllowingStateLoss();
        getSupportFragmentManager().executePendingTransactions();
        listFragment.setIssues(myIssues);
    }

    private void showInfoAboutFailedAccounts(int failedAccounts) {
        if (failedAccounts > 0) {
            Snackbar.make(coordinatorLayout, getString(R.string.failedToReadAccountData, failedAccounts), Snackbar.LENGTH_LONG).show();
        }
    }

    @UiThread
    void showProgress() {
        progressDialog = new MaterialDialog.Builder(this).
                content(R.string.fetchingData).
                progress(true, 0).cancelable(false).show();
    }

    @UiThread
    void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @UiThread
    void initNavigation(List<NavigationModel> navigationModels) {
        final RecyclerViewExpandableItemManager manager = new RecyclerViewExpandableItemManager(null);
        final NavigationMenuAdapter adapter = new NavigationMenuAdapter(this, navigationModels, (project, baseAccount) -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            if(project.getAccountType() == AccountTypes.ACC_JIRA) {
                showJiraFragment((JiraProject) project, baseAccount);
            } else if(project.getAccountType() == AccountTypes.ACC_STASH){
                showStashFragment((StashProject) project, baseAccount);
            }
        });
        final RecyclerView.Adapter wrappedAdapter = manager.createWrappedAdapter(adapter);
        navigationRecycler.setLayoutManager(new LinearLayoutManager(this));
        navigationRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        navigationRecycler.setHasFixedSize(true);
        navigationRecycler.setAdapter(wrappedAdapter);
        manager.expandAll();
        manager.attachRecyclerView(navigationRecycler);
        if (!Once.beenDone(Once.THIS_APP_INSTALL, Consts.SHOW_DRAWER)) {
            drawerLayout.openDrawer(GravityCompat.START);
            Once.markDone(Consts.SHOW_DRAWER);
        }
    }

    private void showStashFragment(StashProject jiraProject, BaseAccount baseAccount) {
        final FragmentManager supportFragmentManager = getSupportFragmentManager();
        if (!(supportFragmentManager.findFragmentById(R.id.listContainer) instanceof StashProjectFragment)) {
            supportFragmentManager.beginTransaction().replace(R.id.listContainer, stashFragment).commit();
            getSupportFragmentManager().executePendingTransactions();
        }
        stashFragment.setProject(jiraProject, baseAccount);
    }

    private void showJiraFragment(JiraProject jiraProject, BaseAccount baseAccount) {
        final FragmentManager supportFragmentManager = getSupportFragmentManager();
        if (!(supportFragmentManager.findFragmentById(R.id.listContainer) instanceof IssueListFragment)) {
            supportFragmentManager.beginTransaction().replace(R.id.listContainer, listFragment).commit();
            getSupportFragmentManager().executePendingTransactions();
        }
        listFragment.setProject(jiraProject, baseAccount);
    }

    private boolean isTwoPane() {
        if (findViewById(R.id.issue_detail_container) != null) {
            mTwoPane = true;
        }
        tracker.sendEvent(SCREEN_ISSUE_LIST, CATEGORY_APP, "isTablet", String.valueOf(mTwoPane));
        return mTwoPane;
    }

    @UiThread
    void showErrorDialog() {
        final AlertDialogWrapper.Builder builder = new AlertDialogWrapper.Builder(this);
        builder.setTitle(R.string.errorDownloading).setMessage(R.string.errorDownloadingMsg).
                setPositiveButton(R.string.retry, (dialog, which) -> {
                    downloadData();
                }).
                setNegativeButton(R.string.quit, (dialog, which) -> {
                    finish();
                }).
                setNeutralButton(R.string.settings, (dialog, which) -> {
                    final Intent settings = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    startActivity(settings);
                }).show();
    }

    @Click(R.id.actionSettings)
    void onSettingsAction() {
        drawerLayout.closeDrawer(GravityCompat.START);
        showNotImplemented();
    }

    private void showNotImplemented() {
        Snackbar.make(coordinatorLayout, R.string.optionUnavailable, Snackbar.LENGTH_LONG).show();
    }

    @Click(R.id.actionAssignedToMe)
    void onAssignedToMeAction() {
        tracker.sendEvent(SCREEN_ISSUE_LIST, CATEGORY_MENU, "viewAssignedToMe");
        drawerLayout.closeDrawer(GravityCompat.START);
        initMyIssues(myIssues);
    }

    @Click(R.id.actionAccounts)
    void onAccountAction() {
        drawerLayout.closeDrawer(GravityCompat.START);
        startAccountsActivity(false);
    }

    @Click(R.id.actionAbout)
    void onAboutAction() {
        tracker.sendEvent(SCREEN_ISSUE_LIST, CATEGORY_MENU, "viewAbout");
        drawerLayout.closeDrawer(GravityCompat.START);
        showAboutDialog();
    }

    private void showAboutDialog() {
        final MaterialDialog.Builder dialog = new MaterialDialog.Builder(this);
        dialog.customView(R.layout.dialog_about_app, true);
        dialog.cancelable(true);
        final MaterialDialog build = dialog.build();
        build.setOnShowListener(dialog1 -> {
            ((TextView) build.findViewById(R.id.appVersion)).setText(getString(R.string.appVersion, BuildConfig.VERSION_NAME));
            ((TextView) build.findViewById(R.id.copyright)).setText(getString(R.string.copyrightInfo, DateTime.now().getYear()));
        });
        build.show();
    }

    @Click(R.id.actionFeedback)
    void onFeedbackAction() {
        drawerLayout.closeDrawer(GravityCompat.START);
        final FeedbackSettings settings = new FeedbackSettings();
        settings.setCancelButtonText(getString(R.string.cancel));
        settings.setSendButtonText(getString(R.string.submit));
        settings.setText(getString(R.string.feedbackInfo));
        settings.setTitle(getString(R.string.feedback));
        settings.setYourComments(getString(R.string.feedbackYourComments));
        settings.setToast(getString(R.string.feedbackThanks));
        settings.setReplyTitle(getString(R.string.feedbackResponse));
        settings.setReplyCloseButtonText(getString(R.string.close));
        settings.setReplyRateButtonText(getString(R.string.rate));
        settings.setModal(true);
        feedbackDialog.setSettings(settings);
        feedbackDialog.show();
    }

    @OnActivityResult(ACCOUNTS_REQUEST)
    void onAccountAdded(int resultCode) {
        Logger.w("" + resultCode);
        if (resultCode == RESULT_OK) {
            downloadData();
        } else if (accountsDao.getAll().isEmpty()) {
            showNoAccountsDialog();
        }
    }

    @OnActivityResult(NEW_ISSUE_REQUEST)
    void onIssueAdded(int resultCode) {
        Logger.w("" + resultCode);

        if (resultCode == RESULT_OK) {
            listFragment.reload();
        }
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        if (feedbackDialog != null) {
            feedbackDialog.dismiss();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        EventBus.getDefault().register(this);
        super.onResume();
    }

    @Override
    public void onShowBranchesList(@NonNull StashProject project, @NonNull String repoSlug) {
        if(mTwoPane){
            final BranchListFragment fragment = BranchListFragment_.builder().
                    project(project).
                    repoSlug(repoSlug).
                    build();
            setDetailsFragment(fragment, "BRANCHES");
        } else {
            StashDetailsActivity_.intent(this).
                    stashAction(StashDetailsActivity.ACTION_BRANCHES).
                    project(project).
                    repoSlug(repoSlug).
                    start();
        }
    }

    @Override
    public void onShowCommitsList(@NonNull StashProject project, @NonNull String repoSlug) {
        if(mTwoPane){
            final CommitListFragment fragment = CommitListFragment_.builder().
                    project(project).
                    repoSlug(repoSlug).
                    build();
            setDetailsFragment(fragment, "COMMITS");
        } else {
            StashDetailsActivity_.intent(this).
                    stashAction(StashDetailsActivity.ACTION_COMMITS).
                    project(project).
                    repoSlug(repoSlug).
                    start();
        }
    }

    @Override
    public void onShowPullRequestList(StashProject project, String repoSlug, BaseAccount baseAccount) {
        if(mTwoPane){
            final PullRequestListFragment fragment = PullRequestListFragment_.builder().
                    stashProject(project).
                    repoSlug(repoSlug).
                    build();
            setDetailsFragment(fragment, "COMMITS");
        } else {
            StashDetailsActivity_.intent(this).
                    stashAction(StashDetailsActivity.ACTION_PULL_REQUESTS).
                    project(project).
                    repoSlug(repoSlug).
                    start();
        }
    }
}
