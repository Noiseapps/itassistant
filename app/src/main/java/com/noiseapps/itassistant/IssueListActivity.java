package com.noiseapps.itassistant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.noiseapps.itassistant.adapters.NavigationMenuAdapter;
import com.noiseapps.itassistant.connector.JiraConnector;
import com.noiseapps.itassistant.database.dao.AccountsDao;
import com.noiseapps.itassistant.fragment.IssueDetailFragment;
import com.noiseapps.itassistant.fragment.IssueListFragment;
import com.noiseapps.itassistant.model.NavigationModel;
import com.noiseapps.itassistant.model.account.AccountTypes;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.jira.projects.JiraProject;
import com.noiseapps.itassistant.model.jira.session.SessionResponse;
import com.noiseapps.itassistant.model.jira.user.JiraUser;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@EActivity(R.layout.activity_issue_app_bar)
public class IssueListActivity extends AppCompatActivity
        implements IssueListFragment.Callbacks {

    private boolean mTwoPane;

    @ViewById
    Toolbar toolbar;
    @ViewById
    RecyclerView recyclerView;

    @Bean
    AccountsDao accountsDao;
    @Bean
    JiraConnector jiraConnector;
    private ProgressDialog progressDialog;

    @AfterViews
    void init() {
        if(accountsDao.getAll().isEmpty()) {
            accountsDao.add(new BaseAccount(0, "tomasz.scibiorek", "kotek77@", "http://jira.exaco.pl", "", AccountTypes.ACC_JIRA));
        }
        downloadData();

        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        isTwoPane();
    }

    @Background
    void downloadData() {
        final long start = System.currentTimeMillis();
        showProgress();
        final List<NavigationModel> navigationModels = new ArrayList<>();
        for (final BaseAccount baseAccount : accountsDao.getAll()) {
            jiraConnector.setCurrentConfig(baseAccount);
            jiraConnector.createSession(new Callback<SessionResponse>() {
                @Override
                public void success(SessionResponse sessionResponse, Response response) {
                    jiraConnector.getUserData(new Callback<JiraUser>() {
                        @Override
                        public void success(final JiraUser jiraUser, Response response) {
                            jiraConnector.getUserProjects(new Callback<List<JiraProject>>() {
                                @Override
                                public void success(List<JiraProject> jiraProjects, Response response) {
                                    navigationModels.add(new NavigationModel(jiraUser, jiraProjects));
                                    initNavigation(navigationModels);
                                    hideProgress();
                                    Logger.d("Downloading : " + (System.currentTimeMillis() - start) + " ms");
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    navigationModels.clear();
                                    hideProgress();
                                }
                            });
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            navigationModels.clear();
                            hideProgress();
                        }
                    });
                }

                @Override
                public void failure(RetrofitError error) {
                    navigationModels.clear();
                    hideProgress();
                }
            });
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
        if(progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void initNavigation(List<NavigationModel> navigationModels) {
        final RecyclerViewExpandableItemManager manager = new RecyclerViewExpandableItemManager(null);
        final NavigationMenuAdapter adapter = new NavigationMenuAdapter(this, navigationModels);
        final RecyclerView.Adapter wrappedAdapter = manager.createWrappedAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(wrappedAdapter);
        manager.attachRecyclerView(recyclerView);
    }

    private void isTwoPane() {
        if (findViewById(R.id.issue_detail_container) != null) {
            mTwoPane = true;
        }
    }

    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            final Bundle arguments = new Bundle();
            arguments.putString(IssueDetailFragment.ARG_ITEM_ID, id);
            final IssueDetailFragment fragment = new IssueDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.issue_detail_container, fragment)
                    .commit();

        } else {
            // TODO change to AA
            final Intent detailIntent = new Intent(this, IssueDetailActivity.class);
            detailIntent.putExtra(IssueDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }
}
