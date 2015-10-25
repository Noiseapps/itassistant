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

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@EFragment(R.layout.fragment_jira_list)
public class JiraIssueListFragment extends Fragment {
    @FragmentArg
    String workflowName;
    @FragmentArg
    boolean assignedToMe;
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
        issuesRecycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
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

    private void showAssigneeDialog(Issue issue) {
        Snackbar.make(issuesRecycler, R.string.optionUnavailable, Snackbar.LENGTH_LONG).show();
//        final List<Transition> transitions = issue.getTransitions();
//        final String[] items = new String[transitions.size()];
//        for (int i = 0; i < transitions.size(); i++) {
//            final Transition transition = transitions.get(i);
//            items[i] = transition.getName();
//        }
//
//        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle(R.string.selectTransition);
//        builder.setItems(items, (dialog, which) -> {
//            callback.showFabProgress();
//            final Transition transition = transitions.get(which);
//            jiraConnector.transitionTo(issue, transition, new Callback<Response>() {
//                @Override
//                public void success(Response response, Response response2) {
//                    callback.hideFabProgress(true);
//                    Snackbar.make(issuesRecycler, R.string.statusChanged, Snackbar.LENGTH_LONG).show();
//                }
//
//                @Override
//                public void failure(RetrofitError error) {
//                    callback.hideFabProgress(false);
//                    Snackbar.make(issuesRecycler, R.string.statusNotChanged, Snackbar.LENGTH_LONG).show();
//                }
//            });
//        });
//        builder.show();
    }

}