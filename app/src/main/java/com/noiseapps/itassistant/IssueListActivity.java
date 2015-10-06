package com.noiseapps.itassistant;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.provider.Settings;
import android.support.annotation.Nullable;
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
import com.noiseapps.itassistant.model.NavigationModel;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.jira.issues.Issue;
import com.noiseapps.itassistant.model.jira.projects.JiraProject;
import com.noiseapps.itassistant.model.jira.user.JiraUser;
import com.noiseapps.itassistant.utils.DividerItemDecoration;
import com.noiseapps.itassistant.utils.events.EventBusAction;
import com.noiseapps.itassistant.utils.events.OpenDrawerEvent;
import com.orhanobut.logger.Logger;

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
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@EActivity(R.layout.activity_issue_app_bar)
public class IssueListActivity extends AppCompatActivity
        implements IssueListFragment.Callbacks {

    public static final int ACCOUNTS_REQUEST = 633;
    private boolean mTwoPane;

    @ViewById
    Toolbar toolbar;
    @ViewById
    RecyclerView recyclerView;
    @ViewById
    View mainLayout, emptyList;
    @ViewById
    DrawerLayout drawerLayout;
    @FragmentById(R.id.issue_list)
    IssueListFragment listFragment;

    @Bean
    AccountsDao accountsDao;
    @Bean
    JiraConnector jiraConnector;
    private ProgressDialog progressDialog;
    private NavigationMenuAdapter adapter;

    @AfterViews
    void init() {
        setTablet();
        mainLayout.setVisibility(View.GONE);
        if(accountsDao.getAll().isEmpty()) {
            showNoAccountsDialog();
        } else {
            downloadData();
        }
        initToolbar();
        isTwoPane();
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
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startAccountsActivity();
            }
        });
        builder.setNegativeButton(R.string.quit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }

    private void startAccountsActivity() {
        AccountsActivity_.intent(this).startForResult(ACCOUNTS_REQUEST);
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
        final List<NavigationModel> navigationModels = new ArrayList<>();
        for (final BaseAccount baseAccount : accountsDao.getAll()) {
            jiraConnector.setCurrentConfig(baseAccount);
            jiraConnector.getUserData(new Callback<JiraUser>() {
                @Override
                public void success(final JiraUser jiraUser, Response response) {
                    jiraConnector.getUserProjects(new Callback<List<JiraProject>>() {
                        @Override
                        public void success(List<JiraProject> jiraProjects, Response response) {
                            navigationModels.add(new NavigationModel(baseAccount, jiraUser, jiraProjects));
                            initNavigation(navigationModels);
                            hideProgress();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            showErrorDialog();
                            navigationModels.clear();
                            hideProgress();
                        }
                    });
                }

                @Override
                public void failure(RetrofitError error) {
                    // TODO error
                    navigationModels.clear();
                    hideProgress();
                }
            });
        }
    }

    @UiThread
    void showErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.errorDownloading).setMessage(R.string.errorDownloadingMsg).
                setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).
                setNeutralButton(R.string.settings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Intent settings = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        startActivity(settings);
                    }
                }).show();
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
        if(progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void initNavigation(List<NavigationModel> navigationModels) {
        final RecyclerViewExpandableItemManager manager = new RecyclerViewExpandableItemManager(null);
        adapter = new NavigationMenuAdapter(this, navigationModels, new NavigationMenuAdapter.AdapterCallbacks() {
            @Override
            public void onItemClicked(JiraProject jiraProject, BaseAccount baseAccount) {
                mainLayout.setVisibility(View.VISIBLE);
                emptyList.setVisibility(View.GONE);
                drawerLayout.closeDrawer(GravityCompat.START);
                listFragment.setProject(jiraProject, baseAccount);
            }
        });
        final RecyclerView.Adapter wrappedAdapter = manager.createWrappedAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(wrappedAdapter);
        manager.attachRecyclerView(recyclerView);
        drawerLayout.openDrawer(GravityCompat.START);
    }

    private void isTwoPane() {
        if (findViewById(R.id.issue_detail_container) != null) {
            mTwoPane = true;
        }
    }

    @Override
    public void onItemSelected(Issue issue) {
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
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        EventBus.getDefault().register(this);
        super.onResume();
    }

    @Click(R.id.openDrawer)
    void openDrawer() {
        onEvent(null);
    }

    @EventBusAction
    public void onEvent(@Nullable OpenDrawerEvent event) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Click(R.id.actionAccounts)
    void onAccountAction() {
        startAccountsActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.w("" + requestCode);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnActivityResult(ACCOUNTS_REQUEST)
    void onAccountAdded(int resultCode) {
        Logger.w("" + resultCode);
        if(resultCode == RESULT_OK) {
            downloadData();
        } else if (accountsDao.getAll().isEmpty()) {
            showNoAccountsDialog();
        }
    }
}
