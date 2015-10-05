package com.noiseapps.itassistant.fragment;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.adapters.IssuesAdapter;
import com.noiseapps.itassistant.connector.JiraConnector;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.jira.issues.Issue;
import com.noiseapps.itassistant.model.jira.issues.JiraIssue;
import com.noiseapps.itassistant.model.jira.projects.JiraProject;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@EFragment(R.layout.fragment_issue_list)
public class IssueListFragment extends Fragment {

    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    private Callbacks mCallbacks = sDummyCallbacks;
    private JiraProject jiraProject;
    @Bean
    JiraConnector jiraConnector;
    private IssuesAdapter adapter;
    @ViewById(R.id.issueList)
    RecyclerView recyclerView;
    @ViewById
    LinearLayout loadingView, emptyView;
    private List<Issue> issues;

    public void setProject(JiraProject jiraProject, BaseAccount baseAccount) {
        this.jiraProject = jiraProject;
        showProgress();
        getIssues(baseAccount);
    }

    private void showProgress() {
        loadingView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Background
    void getIssues(BaseAccount baseAccount) {
        jiraConnector.setCurrentConfig(baseAccount);
        jiraConnector.getProjectIssues(jiraProject.getKey(), new Callback<JiraIssue>() {
            @Override
            public void success(JiraIssue jiraIssue, Response response) {
                updateListAdapter(jiraIssue);
            }

            @Override
            public void failure(RetrofitError error) {
                updateListAdapter(null);
            }
        });
    }

    private void updateListAdapter(@Nullable JiraIssue jiraIssue) {
        issues = jiraIssue == null ? new ArrayList<Issue>() : jiraIssue.getIssues();
        adapter = new IssuesAdapter(getContext(), issues);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        hideProgress();
    }

    @UiThread
    void hideProgress() {
        loadingView.setVisibility(View.GONE);
        if(issues.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public interface Callbacks {
        void onItemSelected(String id);
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = sDummyCallbacks;
    }
}
