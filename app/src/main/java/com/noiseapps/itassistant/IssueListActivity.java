package com.noiseapps.itassistant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.noiseapps.itassistant.adapters.NavigationMenuAdapter;
import com.noiseapps.itassistant.connector.JiraConnector;
import com.noiseapps.itassistant.database.dao.AccountsDao;
import com.noiseapps.itassistant.fragment.IssueDetailFragment;
import com.noiseapps.itassistant.fragment.IssueDetailFragment_;
import com.noiseapps.itassistant.fragment.IssueListFragment;
import com.noiseapps.itassistant.fragment.NewIssueFragment;
import com.noiseapps.itassistant.fragment.NewIssueFragment_;
import com.noiseapps.itassistant.model.NavigationModel;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.jira.issues.Issue;
import com.noiseapps.itassistant.model.jira.projects.JiraProject;
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
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;
import jonathanfinerty.once.Once;

@EActivity(R.layout.activity_issue_app_bar)
public class IssueListActivity extends AppCompatActivity
        implements IssueListFragment.Callbacks, NewIssueFragment.NewIssueCallbacks, IssueDetailFragment.IssueDetailCallbacks {

    public static final int ACCOUNTS_REQUEST = 633;
    public static final int NEW_ISSUE_REQUEST = 5135;
    @ViewById
    Toolbar toolbar;
    @ViewById
    RecyclerView recyclerView;
    @ViewById
    View mainLayout;
    @ViewById
    DrawerLayout drawerLayout;
    @FragmentById(R.id.issue_list)
    IssueListFragment listFragment;
    @Bean
    AccountsDao accountsDao;
    @Bean
    JiraConnector jiraConnector;
    ArrayList<NavigationModel> navigationModels;
    private boolean mTwoPane;
    private ProgressDialog progressDialog;
    private ArrayList<Issue> myIssues;
    private FeedbackDialog feedbackDialog;

    @Override
    public void onItemSelected(Issue issue, JiraProject jiraProject) {
        if (mTwoPane) {
            final IssueDetailFragment fragment = IssueDetailFragment_.builder().issue(issue).build();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.issue_detail_container, fragment)
                    .commit();
        } else {
            IssueDetailActivity_.intent(this).issue(issue).start();
        }
    }

    @Override
    public void onAddNewIssue(JiraProject jiraProject) {
        final String key = jiraProject.getKey();
        if (mTwoPane) {
            final NewIssueFragment fragment = NewIssueFragment_.builder().projectKey(key).build();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.issue_detail_container, fragment)
                    .commit();
        } else {
            NewIssueActivity_.intent(this).projectKey(key).startForResult(NEW_ISSUE_REQUEST);
        }
    }

    @Override
    public void onEditIssue(Issue issue) {
        final String key = issue.getFields().getProject().getKey();
        if (mTwoPane) {
            final NewIssueFragment fragment = NewIssueFragment_.builder().projectKey(key).issue(issue).build();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.issue_detail_container, fragment)
                    .commit();
        } else {
            NewIssueActivity_.intent(this).projectKey(key).issue(issue).startForResult(NEW_ISSUE_REQUEST);
        }
    }

    @EventBusAction
    public void onEvent(@Nullable OpenDrawerEvent event) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onIssueCreated() {
        listFragment.reload();
    }

    @AfterViews
    void init() {
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
    }

    private void setTablet() {
        if (getResources().getBoolean(R.bool.tabletSize)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
    }

    private void showNoAccountsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
            try {
                jiraConnector.setCurrentConfig(baseAccount);
                AuthenticatedPicasso.setConfig(this, baseAccount);
                final List<JiraProject> jiraProjects = jiraConnector.getUserProjects();
                if (jiraProjects != null) {
                    navigationModels.add(new NavigationModel(baseAccount, jiraProjects));
                } else {
                    failedAccounts++;
                }
                final List<Issue> myProjectIssues = jiraConnector.getAssignedToMe();
                myIssues.addAll(myProjectIssues);
            } catch (Exception e) {
                Logger.e(e, e.getMessage());
            }
        }
        hideProgress();
        showInfoAboutFailedAccounts(failedAccounts);
        initMyIssues(myIssues);
        initNavigation(navigationModels);
    }

    @UiThread
    void initMyIssues(List<Issue> myIssues) {
        listFragment.setIssues(myIssues);
    }

    private void showInfoAboutFailedAccounts(int failedAccounts) {
        if (failedAccounts > 0) {
            Snackbar.make(recyclerView, getString(R.string.failedToReadAccountData, failedAccounts), Snackbar.LENGTH_LONG).show();
        }
    }

    @UiThread
    void showProgress() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle(R.string.fetchingData);
        progressDialog.show();
    }

    @UiThread
    void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @UiThread
    void initNavigation(List<NavigationModel> navigationModels) {
        final RecyclerViewExpandableItemManager manager = new RecyclerViewExpandableItemManager(null);
        final NavigationMenuAdapter adapter = new NavigationMenuAdapter(this, navigationModels, (jiraProject, baseAccount) -> {
            mainLayout.setVisibility(View.VISIBLE);
            drawerLayout.closeDrawer(GravityCompat.START);
            listFragment.setProject(jiraProject, baseAccount);
        });
        final RecyclerView.Adapter wrappedAdapter = manager.createWrappedAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(wrappedAdapter);
        manager.expandAll();
        manager.attachRecyclerView(recyclerView);
        if (!Once.beenDone(Once.THIS_APP_INSTALL, Consts.SHOW_DRAWER)) {
            drawerLayout.openDrawer(GravityCompat.START);
            Once.markDone(Consts.SHOW_DRAWER);
        }
    }

    private void isTwoPane() {
        if (findViewById(R.id.issue_detail_container) != null) {
            mTwoPane = true;
        }
    }

    @UiThread
    void showErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

//    @Click(R.id.openDrawer)
//    void openDrawer() {
//        onEvent(null);
//    }

    @Click(R.id.actionSettings)
    void onSettingsAction() {
        showNotImplemented();
    }

    private void showNotImplemented() {
        Snackbar.make(drawerLayout, R.string.optionUnavailable, Snackbar.LENGTH_LONG).show();
    }

    @Click(R.id.actionAssignedToMe)
    void onAssignedToMeAction() {
        initMyIssues(myIssues);
    }

    @Click(R.id.actionAccounts)
    void onAccountAction() {
        startAccountsActivity(false);
    }

    @Click(R.id.actionAbout)
    void onAboutAction() {
        showNotImplemented();
    }

    @Click(R.id.actionFeedback)
    void onFeedbackAction() {
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
        if(feedbackDialog != null) {
            feedbackDialog.dismiss();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        EventBus.getDefault().register(this);
        super.onResume();
    }
}
