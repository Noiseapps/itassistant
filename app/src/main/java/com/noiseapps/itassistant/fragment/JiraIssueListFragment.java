package com.noiseapps.itassistant.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.adapters.IssuesAdapter;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.jira.issues.Issue;
import com.noiseapps.itassistant.utils.AuthenticatedPicasso;
import com.noiseapps.itassistant.utils.DividerItemDecoration;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_jira_list)
public class JiraIssueListFragment extends Fragment{
    @FragmentArg
    String workflowName;
    @FragmentArg
    BaseAccount apiConfig;
    Activity context;

    @ViewById
    RecyclerView issuesRecycler;
    private List<Issue> issues;

    @AfterViews
    void init() {
        context = getActivity();
        setAdapter();
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

    private void setAdapter() {
        issuesRecycler.setLayoutManager(new LinearLayoutManager(context));
        final IssuesAdapter adapter = new IssuesAdapter(context, issues);
        issuesRecycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        adapter.setPicasso(AuthenticatedPicasso.getAuthPicasso(context, apiConfig));
        issuesRecycler.setAdapter(adapter);
    }
}
