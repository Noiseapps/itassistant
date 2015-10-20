package com.noiseapps.itassistant.fragment;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.adapters.IssuesAdapter;
import com.noiseapps.itassistant.connector.JiraConnector;
import com.noiseapps.itassistant.model.jira.issues.Issue;
import com.noiseapps.itassistant.model.jira.issues.Transition;
import com.noiseapps.itassistant.utils.AuthenticatedPicasso;
import com.noiseapps.itassistant.utils.DividerItemDecoration;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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
        final IssuesAdapter adapter = new IssuesAdapter(context, issues, authPicasso, new AdapterCallback());
        issuesRecycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        issuesRecycler.setAdapter(adapter);
    }

    private class AdapterCallback implements IssuesAdapter.IssueAdapterCallback {
        @Override
        public void onItemClicked(Issue selectedIssue) {
            callback.onItemSelected(selectedIssue);
        }

        @Override
        public void onItemLongPressed(Issue issue) {
            showContextInfoDialog(issue);
        }
    }

    private void showContextInfoDialog(Issue issue) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.issueMoreInfo);
        builder.setItems(getActivity().getResources().getStringArray(R.array.moreIssueActions),
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            callback.onItemEdit(issue);
                            break;
                        case 1:
                            showTransitionDialog(issue);
                            break;
                        case 2:
                            showAssigneeDialog(issue);
                            break;
                    }
                    dialog.dismiss();
                });
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
            dialog.dismiss();
        });
        builder.show();
    }

    private void showTransitionDialog(Issue issue) {
        final List<Transition> transitions = issue.getTransitions();
        final String[] items = new String[transitions.size()];
        for (int i = 0; i < transitions.size(); i++) {
            final Transition transition = transitions.get(i);
            items[i] = transition.getName();
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.selectTransition);
        builder.setItems(items, (dialog, which) -> {
            callback.showFabProgress();
            final Transition transition = transitions.get(which);
            jiraConnector.transitionTo(issue, transition, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    callback.hideFabProgress(true);
                }

                @Override
                public void failure(RetrofitError error) {
                    callback.hideFabProgress(false);
                }
            });
        });
        builder.show();
    }

    private void showAssigneeDialog(Issue issue) {
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