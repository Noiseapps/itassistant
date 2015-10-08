package com.noiseapps.itassistant.fragment;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.adapters.newissue.AssigneeSpinnerAdapter;
import com.noiseapps.itassistant.adapters.newissue.PrioritySpinnerAdapter;
import com.noiseapps.itassistant.adapters.newissue.TypeSpinnerAdapter;
import com.noiseapps.itassistant.connector.JiraConnector;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.jira.issues.Assignee;
import com.noiseapps.itassistant.model.jira.issues.Issue;
import com.noiseapps.itassistant.model.jira.issues.Priority;
import com.noiseapps.itassistant.model.jira.projects.JiraProject;
import com.noiseapps.itassistant.model.jira.projects.createmeta.CreateMetaModel;
import com.noiseapps.itassistant.model.jira.projects.createmeta.Project;
import com.noiseapps.itassistant.model.jira.projects.details.JiraProjectDetails;
import com.noiseapps.itassistant.utils.AuthenticatedPicasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@EFragment(R.layout.fragment_new_issue)
public class NewIssueFragment extends Fragment {

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
        if (issue != null) {
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
//        jiraConnector.getCreateMeta(project.getKey(), new Callback<CreateMetaModel>() {
//            @Override
//            public void success(CreateMetaModel createMetaModel, Response response) {
//                showForm(createMetaModel);
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//
//            }
//        });

        jiraConnector.getProjectDetails(project.getId(), new GetDetailsCallback());
    }

//    private void showForm(CreateMetaModel createMetaModel) {
//        hideProgress();
//        noProjectData.setVisibility(View.GONE);
//        newIssueForm.setVisibility(View.VISIBLE);
//        fillForm(createMetaModel);
//    }
//
//    private void fillForm(CreateMetaModel createMetaModel) {
//        final BaseAccount currentConfig = jiraConnector.getCurrentConfig();
//        final Project project = createMetaModel.getProjects().get(0);
//        new TypeSpinnerAdapter(getActivity(), project.getIssueTypes());
//        new PrioritySpinnerAdapter(getActivity(), project.getIssueTypes().get(0).getFields().getPriority().getAllowedValues());
//        new AssigneeSpinnerAdapter(getActivity(), project.getIssueTypes().get(0).getFields().getAssignee().)
//    }

    private void showForm(JiraProjectDetails jiraProjectDetails, List<Assignee> assignees, List<Priority> priorities) {
        hideProgress();
        noProjectData.setVisibility(View.GONE);
        newIssueForm.setVisibility(View.VISIBLE);
        fillForm(jiraProjectDetails, assignees, priorities);
    }

    private void fillForm(JiraProjectDetails jiraProjectDetails, final List<Assignee> assignees, List<Priority> priorities) {
        final BaseAccount currentConfig = jiraConnector.getCurrentConfig();
//        issueTypeSpinner.setAdapter(new TypeSpinnerAdapter(getContext(), jiraProjectDetails.getIssueTypes()));
//        issuePrioritySpinner.setAdapter(new PrioritySpinnerAdapter(getContext(), priorities));
//        assigneeSpinner.setAdapter(new AssigneeSpinnerAdapter(getContext(), assignees, AuthenticatedPicasso.getAuthPicasso(getContext(), currentConfig)));
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
            jiraConnector.getIssuePriorities(new GetPrioritiesCallback(jiraProjectDetails, assignees));
        }

        @Override
        public void failure(RetrofitError error) {
            showError();
        }
    }

    private class GetPrioritiesCallback implements Callback<List<Priority>> {
        private final JiraProjectDetails jiraProjectDetails;
        private final List<Assignee> assignees;

        public GetPrioritiesCallback(JiraProjectDetails jiraProjectDetails, List<Assignee> assignees) {
            this.jiraProjectDetails = jiraProjectDetails;
            this.assignees = assignees;
        }

        @Override
        public void success(List<Priority> priorities, Response response) {
            showForm(jiraProjectDetails, assignees, priorities);
        }

        @Override
        public void failure(RetrofitError error) {
            showError();
        }
    }

}
