package com.noiseapps.itassistant.fragment;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.adapters.AssigneeSpinnerAdapter;
import com.noiseapps.itassistant.connector.JiraConnector;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.jira.issues.Assignee;
import com.noiseapps.itassistant.model.jira.issues.Issue;
import com.noiseapps.itassistant.model.jira.projects.JiraProject;
import com.noiseapps.itassistant.model.jira.projects.details.JiraProjectDetails;
import com.noiseapps.itassistant.utils.AuthenticatedPicasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@EFragment(R.layout.fragment_new_issue)
public class NewIssueFragment extends Fragment{

    @FragmentArg
    Issue issue;

    @FragmentArg
    JiraProject project;
    @ViewById
    LinearLayout fetchingDataProgress, noProjectData, newIssueForm;
    @ViewById
    Spinner issueTypeSpinner, issuePrioritySpinner, assigneeSpinner;
    @ViewById
    FloatingActionButton saveIssueFab;
    @ViewById
    FABProgressCircle fabProgressCircle;
    @Bean
    JiraConnector jiraConnector;

    @AfterViews
    void init() {
        getProjectDetails();
        if(issue != null) {
            initValues();
        }
    }

    private void initValues() {
        
    }

    @Click(R.id.saveIssueFab)
    void onSaveIssue() {
        Snackbar.make(fabProgressCircle, R.string.optionUnavailable, Snackbar.LENGTH_LONG).show();
    }

    void getProjectDetails() {
        fetchingDataProgress.setVisibility(View.VISIBLE);
        jiraConnector.getProjectDetails(project.getId(), new GetDetailsCallback());
    }

    private void showForm(JiraProjectDetails jiraProjectDetails, List<Assignee> assignees) {
        hideProgress();
        noProjectData.setVisibility(View.GONE);
        newIssueForm.setVisibility(View.VISIBLE);
        fillForm(jiraProjectDetails, assignees);
    }

    private void fillForm(JiraProjectDetails jiraProjectDetails, final List<Assignee> assignees) {
        final BaseAccount currentConfig = jiraConnector.getCurrentConfig();
        assigneeSpinner.setAdapter(new AssigneeSpinnerAdapter(getContext(), assignees, AuthenticatedPicasso.getAuthPicasso(getContext(), currentConfig)));
        assigneeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), assignees.get(position).getDisplayName(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showError() {
        hideProgress();
        noProjectData.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        fetchingDataProgress.setVisibility(View.GONE);
    }

    private class GetDetailsCallback implements Callback<JiraProjectDetails> {
        @Override
        public void success(final JiraProjectDetails jiraProjectDetails, Response response) {
            jiraConnector.getProjectMembers(project.getKey(), new GetMembersCallback(jiraProjectDetails));
        }

        @Override
        public void failure(RetrofitError error) {
            showError();
        }
    }

    private class GetMembersCallback implements Callback<List<Assignee>> {
        private final JiraProjectDetails jiraProjectDetails;

        public GetMembersCallback(JiraProjectDetails jiraProjectDetails) {
            this.jiraProjectDetails = jiraProjectDetails;
        }

        @Override
        public void success(List<Assignee> assignees, Response response) {
            showForm(jiraProjectDetails, assignees);
        }

        @Override
        public void failure(RetrofitError error) {
            showError();
        }
    }

}
