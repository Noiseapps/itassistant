package com.noiseapps.itassistant.fragment;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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
import com.noiseapps.itassistant.model.jira.projects.JiraProject;
import com.noiseapps.itassistant.model.jira.projects.createissue.CreateIssueModel;
import com.noiseapps.itassistant.model.jira.projects.createissue.CreateIssueModel.Fields.IdField;
import com.noiseapps.itassistant.model.jira.projects.createissue.CreateIssueModel.Fields.NameField;
import com.noiseapps.itassistant.model.jira.projects.createissue.CreateIssueResponse;
import com.noiseapps.itassistant.model.jira.projects.createissue.FieldsBuilder;
import com.noiseapps.itassistant.model.jira.projects.createmeta.AllowedValue;
import com.noiseapps.itassistant.model.jira.projects.createmeta.CreateMetaModel;
import com.noiseapps.itassistant.model.jira.projects.createmeta.IssueType;
import com.noiseapps.itassistant.model.jira.projects.createmeta.Project;
import com.noiseapps.itassistant.utils.AuthenticatedPicasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.List;
import java.util.ListIterator;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@EFragment(R.layout.fragment_new_issue)
public class NewIssueFragment extends Fragment {

    @FragmentArg
    Issue issue;
    @FragmentArg
    Issue parent;
    @FragmentArg
    JiraProject project;
    @ViewById
    LinearLayout fetchingDataProgress, noProjectData, newIssueForm;
    @ViewById
    EditText issueDescription, issueSummary;
    @ViewById
    Spinner issueTypeSpinner, issuePrioritySpinner, assigneeSpinner;
    @ViewById
    FloatingActionButton saveIssueFab;
    @ViewById
    FABProgressCircle fabProgressCircle;
    @Bean
    JiraConnector jiraConnector;
    private BaseAccount currentConfig;
    private NewIssueCallbacks callbacks;


    public interface NewIssueCallbacks {
        void onIssueCreated();
    }

    @AfterViews
    void init() {
        callbacks = (NewIssueCallbacks) getActivity();
        currentConfig = jiraConnector.getCurrentConfig();
        getProjectDetails();
    }

    private void initValues() {

    }

    @Click(R.id.saveIssueFab)
    void onSaveIssue() {
        fabProgressCircle.show();
        final CreateIssueModel.Fields fields = getFields();
        jiraConnector.postNewIssue(new CreateIssueModel(fields), new Callback<CreateIssueResponse>() {
            @Override
            public void success(CreateIssueResponse createIssueResponse, Response response) {
                fabProgressCircle.beginFinalAnimation();
                Snackbar.make(fabProgressCircle, R.string.issueAdded, Snackbar.LENGTH_LONG).show();
                callbacks.onIssueCreated();
                //TODO show issue details if in two pane
            }

            @Override
            public void failure(RetrofitError error) {
                fabProgressCircle.hide();
                Snackbar.make(fabProgressCircle, R.string.failedToPostIssue, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private CreateIssueModel.Fields getFields() {
        final FieldsBuilder builder = new FieldsBuilder();
        builder.setProject(new IdField(project.getId()));
        final AllowedValue selectedValue = (AllowedValue) issuePrioritySpinner.getSelectedItem();
        builder.setPriority(new IdField(selectedValue.getId()));
        final IssueType selectedType = (IssueType) issueTypeSpinner.getSelectedItem();
        builder.setIssuetype(new IdField(selectedType.getId()));
        builder.setSummary(issueSummary.getText().toString());
        builder.setDescription(issueDescription.getText().toString());
        builder.setReporter(new NameField(currentConfig.getUsername()));
        final Assignee selectedAssignee = (Assignee) assigneeSpinner.getSelectedItem();
        builder.setAssignee(new NameField(selectedAssignee.getName()));
        return builder.createFields();
    }

    void getProjectDetails() {
        fetchingDataProgress.setVisibility(View.VISIBLE);
        jiraConnector.getCreateMeta(project.getKey(), new GetCreateMetaCallback());
    }

    private void showForm(CreateMetaModel createMetaModel, List<Assignee> assignees) {
        hideProgress();
        noProjectData.setVisibility(View.GONE);
        newIssueForm.setVisibility(View.VISIBLE);
        fillForm(createMetaModel, assignees);

        if (issue != null) {
            initValues();
        }
    }

    private void fillForm(CreateMetaModel createMetaModel, List<Assignee> assignees) {
        final Project project = createMetaModel.getProjects().get(0);
        final List<IssueType> issueTypes = project.getIssueTypes();
        if (parent != null) {
            filterSubtasks(issueTypes);
        }
        final TypeSpinnerAdapter typeSpinnerAdapter = new TypeSpinnerAdapter(getActivity(), issueTypes);
        final PrioritySpinnerAdapter prioritySpinnerAdapter = new PrioritySpinnerAdapter(getActivity(), issueTypes.get(0).getFields().getPriority().getAllowedValues());
        final AssigneeSpinnerAdapter assigneeSpinnerAdapter = new AssigneeSpinnerAdapter(getActivity(), assignees, AuthenticatedPicasso.getAuthPicasso(getActivity(), currentConfig));
        configureSpinners(issueTypes, typeSpinnerAdapter, prioritySpinnerAdapter, assigneeSpinnerAdapter);
    }

    private void configureSpinners(final List<IssueType> issueTypes, TypeSpinnerAdapter typeSpinnerAdapter, final PrioritySpinnerAdapter prioritySpinnerAdapter, AssigneeSpinnerAdapter assigneeSpinnerAdapter) {
        issueTypeSpinner.setAdapter(typeSpinnerAdapter);
        issueTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                prioritySpinnerAdapter.setItems(issueTypes.get(position).getFields().getPriority().getAllowedValues());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        issuePrioritySpinner.setAdapter(prioritySpinnerAdapter);
        assigneeSpinner.setAdapter(assigneeSpinnerAdapter);
    }

    private void filterSubtasks(List<IssueType> issueTypes) {
        for (ListIterator<IssueType> iterator = issueTypes.listIterator(); iterator.hasNext(); ) {
            final IssueType issueType = iterator.next();
            if (issueType.isSubtask()) {
                iterator.remove();
            }
        }
    }

    private void showError() {
        hideProgress();
        noProjectData.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        fetchingDataProgress.setVisibility(View.GONE);
    }

    private class GetProjectMembersCallback implements Callback<List<Assignee>> {
        private CreateMetaModel createMetaModel;

        public GetProjectMembersCallback(CreateMetaModel createMetaModel) {
            this.createMetaModel = createMetaModel;
        }

        @Override
        public void success(List<Assignee> assignees, Response response) {
            showForm(createMetaModel, assignees);
        }

        @Override
        public void failure(RetrofitError error) {
            showError();
        }
    }

    private class GetCreateMetaCallback implements Callback<CreateMetaModel> {
        @Override
        public void success(final CreateMetaModel createMetaModel, Response response) {
            final GetProjectMembersCallback callback = new GetProjectMembersCallback(createMetaModel);
            jiraConnector.getProjectMembers(project.getKey(), callback);
        }

        @Override
        public void failure(RetrofitError error) {
            showError();
        }
    }

    {
    }
}
