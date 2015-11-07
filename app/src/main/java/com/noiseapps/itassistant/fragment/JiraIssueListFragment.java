package com.noiseapps.itassistant.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.adapters.IssuesAdapter;
import com.noiseapps.itassistant.connector.JiraConnector;
import com.noiseapps.itassistant.model.jira.issues.Assignee;
import com.noiseapps.itassistant.model.jira.issues.Issue;
import com.noiseapps.itassistant.model.jira.issues.Transition;
import com.noiseapps.itassistant.utils.AuthenticatedPicasso;
import com.noiseapps.itassistant.utils.DividerItemDecoration;
import com.noiseapps.itassistant.utils.ToggleList;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import retrofit.client.Response;

@EFragment(R.layout.fragment_jira_list)
public class JiraIssueListFragment extends Fragment {
    @FragmentArg
    String workflowName;
    @FragmentArg
    boolean assignedToMe;
    @FragmentArg
    ArrayList<Assignee> assignees;
    @Bean
    JiraConnector jiraConnector;
    Activity context;

    @ViewById
    RecyclerView issuesRecycler;
    @InstanceState
    ArrayList<Issue> issues;
    private IssueListCallback callback;
    private ActionMode actionMode;
    private IssuesAdapter adapter;
    private ProgressDialog progressDialog;

    public interface IssueListCallback {
        void onItemSelected(Issue selectedIssue);

        void onItemEdit(Issue issue);

        void showFabProgress();

        void hideFabProgress(boolean success);
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
        adapter = new IssuesAdapter(context, issues, authPicasso, new AdapterCallback(), assignedToMe);
        issuesRecycler.setAdapter(adapter);
    }

    private class AdapterCallback implements IssuesAdapter.IssueAdapterCallback {
        @Override
        public void onItemClicked(Issue selectedIssue) {
            if(actionMode != null) {
                actionMode.finish();
            }
            callback.onItemSelected(selectedIssue);
        }

        @Override
        public void onItemLongPressed(ToggleList<Issue> issueToggleList) {
            onLongPressed(issueToggleList);
        }
    }

    private void onLongPressed(ToggleList<Issue> issueToggleList) {
        Logger.d("Issue list size: " + issueToggleList.size());
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        if(actionMode != null) {
            if(issueToggleList.isEmpty()) {
                actionMode.finish();
            }
        } else {
            actionMode = activity.startSupportActionMode(new ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    mode.getMenuInflater().inflate(R.menu.menu_context_issue_list, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.actionTransform:
                            showTransitionDialog(issueToggleList);
                            break;
                        case R.id.actionChangeAssignment:
                            showAssigneeDialog(issueToggleList);
                            break;
                    }
                    return true;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    adapter.clearActionMode();
                    actionMode = null;
                }
            });
        }
    }

    private void showTransitionDialog(ToggleList<Issue> issueList) {
        final List<Transition> transitions = issueList.get(0).getTransitions();
        final String[] items = new String[transitions.size()];
        for (int i = 0; i < transitions.size(); i++) {
            final Transition transition = transitions.get(i);
            items[i] = transition.getName();
        }
        final int[] successful = {0};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.selectTransition);
        builder.setItems(items, (dialog, which) -> {
            showProgress(R.string.changingTransitions);
            progressDialog.setIndeterminate(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(issueList.size());
            progressDialog.show();

            final Transition transition = transitions.get(which);
            transitionItems(issueList, transition, successful);

        });
        builder.show();
    }

    @Background
    void transitionItems(ToggleList<Issue> issueList, Transition transition, int[] unsuccessful) {
        for (Issue issue : issueList) {
            final Response response = jiraConnector.transitionTo(issue, transition);
            incrementProgress();
            if (response == null) {
                unsuccessful[0]++;
            }
        }
        onFinished(unsuccessful[0]);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(actionMode != null) {
            actionMode.finish();
        }
    }

    @UiThread
    void incrementProgress() {
        progressDialog.incrementProgressBy(1);
    }

    @UiThread
    void onFinished(int unsuccessfulCount) {
        if (unsuccessfulCount > 0) {
            Snackbar.make(issuesRecycler, getString(R.string.failedToPostTransition, unsuccessfulCount), Snackbar.LENGTH_LONG).show();
        }
        progressDialog.dismiss();
        actionMode.finish();
        callback.hideFabProgress(true);
    }

    private void showProgress(@StringRes int changingTransitions) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(changingTransitions);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void showAssigneeDialog(ToggleList<Issue> toggleList) {
        final String[] items = new String[assignees.size()+1];
        items[0] = getString(R.string.notAssigned);
        for (int i = 0; i < assignees.size(); i++) {
            final Assignee assignee = assignees.get(i);
            items[i+1] = assignee.getDisplayName();
        }
        final int[] successful = {0};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.selectAssignee);
        builder.setItems(items, (dialog, which) -> {
            showProgress(R.string.changingAssignee);
            progressDialog.setIndeterminate(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(toggleList.size());
            progressDialog.show();

            final Assignee assignee;
            if(which == 0) {
                assignee = new Assignee();
                assignee.setName(null);
            } else {
                assignee = assignees.get(which - 1);
            }
            assignIssues(toggleList, assignee, successful);

        });
        builder.show();
    }

    @Background
    void assignIssues(ToggleList<Issue> toggleList, Assignee assignee, int[] unsuccessful) {
        for (Issue issue : toggleList) {
            final Response response = jiraConnector.changeAssignee(issue.getKey(), assignee.getName());
            incrementProgress();
            if (response == null) {
                unsuccessful[0]++;
            }
        }
        onFinished(unsuccessful[0]);
    }
}