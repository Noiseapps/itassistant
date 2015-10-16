package com.noiseapps.itassistant.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.adapters.IssuesAdapter;
import com.noiseapps.itassistant.connector.JiraConnector;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.jira.issues.Issue;
import com.noiseapps.itassistant.utils.AuthenticatedPicasso;
import com.noiseapps.itassistant.utils.DividerItemDecoration;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_jira_list)
public class JiraIssueListFragment extends Fragment {
    @FragmentArg
    String workflowName;
    @Bean
    JiraConnector jiraConnector;
    Activity context;

    @ViewById
    RecyclerView issuesRecycler;
    @InstanceState
    ArrayList<Issue> issues;
    private IssueListCallback callback;

    public interface IssueListCallback {
        void onItemSelected(Issue selectedIssue);
    }

    @AfterViews
    void init() {
        context = getActivity();
        callback = (IssueListCallback) getParentFragment();
        setAdapter();
    }

    public void setIssues(ArrayList<Issue> issues) {
        this.issues = issues;
    }

    private void setAdapter() {
        issuesRecycler.setLayoutManager(new LinearLayoutManager(context));
        final Picasso authPicasso = AuthenticatedPicasso.getAuthPicasso(context, jiraConnector.getCurrentConfig());
        final IssuesAdapter adapter = new IssuesAdapter(context, issues, authPicasso, new IssuesAdapter.IssueAdapterCallback() {
            @Override
            public void onItemClicked(Issue selectedIssue) {
                callback.onItemSelected(selectedIssue);
            }
        });
        issuesRecycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        issuesRecycler.setAdapter(adapter);
    }
}
