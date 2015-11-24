package com.noiseapps.itassistant.fragment;

import android.app.DatePickerDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.adapters.newissue.AllowedValuesAdapter;
import com.noiseapps.itassistant.adapters.newissue.AssigneeSpinnerAdapter;
import com.noiseapps.itassistant.adapters.newissue.TypeSpinnerAdapter;
import com.noiseapps.itassistant.connector.JiraConnector;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.jira.issues.Assignee;
import com.noiseapps.itassistant.model.jira.issues.Issue;
import com.noiseapps.itassistant.model.jira.projects.createissue.CreateIssueModel;
import com.noiseapps.itassistant.model.jira.projects.createissue.CreateIssueModel.Fields.IdField;
import com.noiseapps.itassistant.model.jira.projects.createissue.CreateIssueModel.Fields.KeyField;
import com.noiseapps.itassistant.model.jira.projects.createissue.CreateIssueModel.Fields.NameField;
import com.noiseapps.itassistant.model.jira.projects.createissue.CreateIssueResponse;
import com.noiseapps.itassistant.model.jira.projects.createmeta.AllowedValue;
import com.noiseapps.itassistant.model.jira.projects.createmeta.CreateMetaModel;
import com.noiseapps.itassistant.model.jira.projects.createmeta.Duedate;
import com.noiseapps.itassistant.model.jira.projects.createmeta.Environment;
import com.noiseapps.itassistant.model.jira.projects.createmeta.Fields;
import com.noiseapps.itassistant.model.jira.projects.createmeta.FixVersions;
import com.noiseapps.itassistant.model.jira.projects.createmeta.IssueType;
import com.noiseapps.itassistant.model.jira.projects.createmeta.Project;
import com.noiseapps.itassistant.model.jira.projects.createmeta.Timetracking;
import com.noiseapps.itassistant.model.jira.projects.createmeta.Versions;
import com.noiseapps.itassistant.utils.AuthenticatedPicasso;
import com.noiseapps.itassistant.utils.Consts;
import com.noiseapps.itassistant.utils.ToggleList;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.MutableDateTime;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EFragment(R.layout.fragment_new_issue)
public class NewIssueFragment extends Fragment {

    @FragmentArg
    Issue issue;
    @FragmentArg
    Issue parent;
    @FragmentArg
    String projectKey;
    @ViewById
    NestedScrollView newIssueForm;
    @ViewById
    TextView estimatedDueDate;
    @ViewById
    MultiAutoCompleteTextView fixedInVersionEditText, versionEditText;
    @ViewById
    LinearLayout noProjectData, fetchingDataProgress, versionContainer, fixVersionContainer;
    @ViewById
    EditText issueDescription, issueSummary, issueEstimatedWorkLog, issueRemainingWorkLog, issueEnvironment;
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
    private IssueType selectedIssueType;
    private MutableDateTime dateTime = new MutableDateTime();
    private boolean[] selectedVersions, selectedFixVersions;
    private ToggleList<AllowedValue> selectedVersionsIds = new ToggleList<>(), selectedFixVersionsIds = new ToggleList<>();

    @AfterViews
    void init() {
        newIssueForm.setVisibility(View.GONE);
        callbacks = (NewIssueCallbacks) getActivity();
        currentConfig = jiraConnector.getCurrentConfig();
        getProjectDetails();
    }

    void getProjectDetails() {
        fetchingDataProgress.setVisibility(View.VISIBLE);
        Observable.zip(
                jiraConnector.getCreateMeta(projectKey),
                jiraConnector.getProjectMembers(projectKey),
                ZipModel::new).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(zipModel -> showForm(zipModel.metaModel, zipModel.assignees), throwable -> {
                    Logger.e(throwable, throwable.getMessage());
                    showError();
                });
    }

    @Click(R.id.saveIssueFab)
    void onSaveIssue() {
        final boolean valid = validate();
        if (!valid) {
            return;
        }
        fabProgressCircle.show();
        final CreateIssueModel.Fields fields = getFields();
        if (issue == null) {
            addNewIssue(fields);
        } else {
            updateIssue(fields);
        }
    }

    private void updateIssue(CreateIssueModel.Fields fields) {
        jiraConnector.updateIssue(issue.getId(), new CreateIssueModel(fields), new Callback<CreateIssueResponse>() {
            @Override
            public void success(CreateIssueResponse createIssueResponse, Response response) {
                fabProgressCircle.beginFinalAnimation();
                Snackbar.make(fabProgressCircle, R.string.issueUpdated, Snackbar.LENGTH_LONG).show();
//                callbacks.onIssueCreated();
                //TODO show issue details if in two pane
            }

            @Override
            public void failure(RetrofitError error) {
                fabProgressCircle.hide();
                Snackbar.make(fabProgressCircle, R.string.failedToPostIssue, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void addNewIssue(CreateIssueModel.Fields fields) {
        jiraConnector.postNewIssue(new CreateIssueModel(fields), new Callback<CreateIssueResponse>() {
            @Override
            public void success(CreateIssueResponse createIssueResponse, Response response) {
                fabProgressCircle.beginFinalAnimation();
                Snackbar.make(fabProgressCircle, R.string.issueAdded, Snackbar.LENGTH_LONG).show();
                callbacks.onIssueCreated();
            }

            @Override
            public void failure(RetrofitError error) {
                fabProgressCircle.hide();
                Snackbar.make(fabProgressCircle, R.string.failedToPostIssue, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private boolean validate() {
        boolean valid = true;
        final Assignee selectedAssignee = (Assignee) assigneeSpinner.getSelectedItem();
        if (selectedIssueType.getFields().getAssignee().isRequired() && selectedAssignee.getName().isEmpty()) {
            valid = false;
        }
        if (selectedIssueType.getFields().getSummary().isRequired() && issueSummary.getText().toString().isEmpty()) {
            issueSummary.setError(getString(R.string.fieldRequired));
            valid = false;
        }
        if (selectedIssueType.getFields().getDescription().isRequired() && issueDescription.getText().toString().isEmpty()) {
            issueDescription.setError(getString(R.string.fieldRequired));
            valid = false;
        }
//        final AllowedValue selectedVersion = (AllowedValue) versionSpinner.getSelectedItem();
//        final Versions versions = selectedIssueType.getFields().getVersions();
//        if (versions != null && versions.isRequired() && selectedVersion.getId().isEmpty()) {
//            valid = false;
//        }
//        final AllowedValue selectedFixVersion = (AllowedValue) fixedInVersionSpinner.getSelectedItem();
//        final FixVersions fixVersions = selectedIssueType.getFields().getFixVersions();
//        if (fixVersions != null && fixVersions.isRequired() && selectedFixVersion.getId().isEmpty()) {
//            valid = false;
//        }
        final Environment environment = selectedIssueType.getFields().getEnvironment();
        if (environment != null && environment.isRequired() && issueEnvironment.getText().toString().isEmpty()) {
            issueEnvironment.setError(getString(R.string.fieldRequired));
            valid = false;
        }

        final String estimatedWorkLog = issueEstimatedWorkLog.getText().toString();
        final Timetracking timetracking = selectedIssueType.getFields().getTimetracking();
        if (timetracking != null && timetracking.isRequired() && estimatedWorkLog.isEmpty()) {
            issueEstimatedWorkLog.setError(getString(R.string.fieldRequired));
            valid = false;
        }
        if (!validateWorkLog(estimatedWorkLog)) {
            issueRemainingWorkLog.setError(getString(R.string.invalidFormat));
            valid = false;
        }

        final String remainingWorkLog = issueRemainingWorkLog.getText().toString();
        if (timetracking != null && timetracking.isRequired() && remainingWorkLog.isEmpty()) {
            issueRemainingWorkLog.setError(getString(R.string.fieldRequired));
            valid = false;
        }
        if (!validateWorkLog(remainingWorkLog)) {
            issueRemainingWorkLog.setError(getString(R.string.invalidFormat));
            valid = false;
        }
        return valid;
    }

    private CreateIssueModel.Fields getFields() {
        final CreateIssueModel.Fields fields = new CreateIssueModel.Fields();
        final Assignee selectedAssignee = (Assignee) assigneeSpinner.getSelectedItem();
        fields.setAssignee(new NameField(selectedAssignee.getName()));
        fields.setProject(new KeyField(projectKey));
        final AllowedValue selectedValue = (AllowedValue) issuePrioritySpinner.getSelectedItem();
        fields.setPriority(new IdField(selectedValue.getId()));
        final IssueType selectedType = (IssueType) issueTypeSpinner.getSelectedItem();
        fields.setIssuetype(new IdField(selectedType.getId()));
        fields.setSummary(issueSummary.getText().toString());
        fields.setDescription(issueDescription.getText().toString());
        setVersions(fields);

        final Duedate duedate = selectedIssueType.getFields().getDuedate();
        final String dueDateString = estimatedDueDate.getText().toString();
        if (duedate != null && !dueDateString.isEmpty()) {
            fields.setDuedate(dueDateString);
        }

        final Timetracking timetracking = selectedIssueType.getFields().getTimetracking();
        if (timetracking != null) {
            fields.setTimetracking(new CreateIssueModel.Timetracking(issueEstimatedWorkLog.getText().toString(), issueRemainingWorkLog.getText().toString()));
        }
        return fields;
    }

    private void setVersions(CreateIssueModel.Fields fields) {
        final ArrayList<IdField> versions = new ArrayList<>();
        for (AllowedValue value : selectedVersionsIds) {
            versions.add(new IdField(value.getId()));
        }
        final ArrayList<IdField> fixVersions = new ArrayList<>();
        for (AllowedValue value : selectedFixVersionsIds) {
            fixVersions.add(new IdField(value.getId()));
        }

        fields.setVersions(versions);
        fields.setFixVersions(fixVersions);
    }

    public boolean validateWorkLog(String workLog) {
        return workLog.isEmpty() || Consts.PATTERN.matcher(workLog).matches();
    }

    private void showForm(CreateMetaModel createMetaModel, List<Assignee> assignees) {
        if (!isAdded()) {
            return;
        }
        hideProgress();
        noProjectData.setVisibility(View.GONE);
        newIssueForm.setVisibility(View.VISIBLE);
        fabProgressCircle.setVisibility(View.VISIBLE);
        fillForm(createMetaModel, assignees);

        if (issue != null) {
            initValues(createMetaModel,assignees);
        }
    }

    @Click(R.id.estimatedDueDate)
    void onSelectEndDate() {
        final DatePickerDialog.OnDateSetListener onDateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            dateTime.setYear(year);
            dateTime.setMonthOfYear(monthOfYear + 1);
            dateTime.setDayOfMonth(dayOfMonth);
            dateTime.setMillisOfDay(0);
            estimatedDueDate.setText(dateTime.toString(Consts.DATE_FORMAT));
        };
        final DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), onDateSetListener, dateTime.getYear(), dateTime.getMonthOfYear() - 1, dateTime.getDayOfMonth());
        datePickerDialog.show();
    }

    private void initValues(CreateMetaModel createMetaModel, List<Assignee> assignees) {
        final com.noiseapps.itassistant.model.jira.issues.Fields fields = issue.getFields();
        issueSummary.setText(fields.getSummary());
        issueDescription.setText(fields.getDescription());
        estimatedDueDate.setText(fields.getDuedate());
        issueEnvironment.setText(fields.getEnvironment());
        setSpinnerPositions(fields);
    }

    private void setSpinnerPositions(com.noiseapps.itassistant.model.jira.issues.Fields fields) {
        final Assignee assignee = fields.getAssignee();
        if(assignee != null) {
            final int spinnerPosition = ((AssigneeSpinnerAdapter) assigneeSpinner.getAdapter()).getPositionForAssignee(assignee);
            assigneeSpinner.setSelection(spinnerPosition);
        }

        final String issueType = fields.getIssueType().getName();
        if(assignee != null) {
            final int spinnerPosition = ((TypeSpinnerAdapter) issueTypeSpinner.getAdapter()).getPositionForValue(issueType);
            issueTypeSpinner.setSelection(spinnerPosition);
        }

        final String issuePriority = fields.getPriority().getName();
        if(assignee != null) {
            final int spinnerPosition = ((AllowedValuesAdapter) issuePrioritySpinner.getAdapter()).getPositionForValue(issuePriority);
            issuePrioritySpinner.setSelection(spinnerPosition);
        }
    }

    private void fillForm(CreateMetaModel createMetaModel, List<Assignee> assignees) {
        final Project project = createMetaModel.getProjects().get(0);
        final List<IssueType> issueTypes = project.getIssueTypes();
        if (parent != null) {
            filterSubtasks(issueTypes);
        }
        configureSpinners(issueTypes, assignees);

    }

    private void addNoItem(List<AllowedValue> allowedVersions) {
        if (!allowedVersions.isEmpty() && allowedVersions.get(0).getId().isEmpty()) {
            return;
        }
        final AllowedValue allowedValue = new AllowedValue();
        allowedValue.setId("");
        allowedValue.setName(getString(R.string.none));
        allowedVersions.add(0, allowedValue);
    }

    private void configureSpinners(final List<IssueType> issueTypes, List<Assignee> assignees) {
        final Fields fields = issueTypes.get(0).getFields();

        final AllowedValuesAdapter priorityAdapter = new AllowedValuesAdapter(getActivity(), fields.getPriority().getAllowedValues());

        final List<AllowedValue> allowedVersions = getVersions(fields);
        final AllowedValuesAdapter versionsAdapter = new AllowedValuesAdapter(getActivity(), allowedVersions);

        final List<AllowedValue> allowedFixVersions = getFixVersions(fields);
        final AllowedValuesAdapter fixedInVersionAdapter = new AllowedValuesAdapter(getActivity(), allowedFixVersions);

        final AssigneeSpinnerAdapter assigneeSpinnerAdapter = new AssigneeSpinnerAdapter(getActivity(), assignees, AuthenticatedPicasso.getAuthPicasso(getActivity(), currentConfig));
        final TypeSpinnerAdapter typeSpinnerAdapter = new TypeSpinnerAdapter(getActivity(), issueTypes);

        setIssueTypeSpinnerSelector(issueTypes, priorityAdapter, versionsAdapter, fixedInVersionAdapter);

        issuePrioritySpinner.setAdapter(priorityAdapter);
        setVersionData(allowedVersions, allowedFixVersions);

        assigneeSpinner.setAdapter(assigneeSpinnerAdapter);
        issueTypeSpinner.setAdapter(typeSpinnerAdapter);
        if (allowedVersions.size() == 1) {
            versionContainer.setVisibility(View.GONE);
        }
        if (allowedFixVersions.size() == 1) {
            versionContainer.setVisibility(View.GONE);
        }
    }

    private void setVersionData(List<AllowedValue> allowedVersions, List<AllowedValue> allowedFixVersions) {
        final String[] versions = new String[allowedVersions.size()];
        final String[] fixVersions = new String[allowedVersions.size()];
        selectedVersionsIds = new ToggleList<>();
        selectedFixVersionsIds = new ToggleList<>();
        selectedVersions = new boolean[allowedVersions.size()];
        selectedFixVersions = new boolean[allowedVersions.size()];

        for (int i = 0; i < allowedVersions.size(); i++) {
            AllowedValue value = allowedVersions.get(i);
            versions[i] = value.getName();
        }
        for (int i = 0; i < allowedFixVersions.size(); i++) {
            AllowedValue value = allowedVersions.get(i);
            fixVersions[i] = value.getName();
        }

        versionEditText.setOnClickListener(v -> {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.issueVersion);
            builder.setMultiChoiceItems(versions, selectedVersions, (dialog, which, isChecked) -> {
                selectedVersions[which] = isChecked;
                selectedVersionsIds.toggle(allowedVersions.get(which));
            });
            builder.setPositiveButton(R.string.ok, (dialog, which) -> {
                versionEditText.setText(StringUtils.join(selectedVersionsIds, ", "));
                dialog.dismiss();
            });
            builder.show();
        });

        fixedInVersionEditText.setOnClickListener(v -> {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.issueVersion);
            builder.setMultiChoiceItems(fixVersions, selectedFixVersions, (dialog, which, isChecked) -> {
                selectedFixVersions[which] = isChecked;
                selectedFixVersionsIds.toggle(allowedFixVersions.get(which));
            });
            builder.setPositiveButton(R.string.ok, (dialog, which) -> {
                fixedInVersionEditText.setText(StringUtils.join(selectedFixVersionsIds, ", "));
                dialog.dismiss();
            });
            builder.show();
        });
    }

    private List<AllowedValue> getVersions(Fields fields) {
        final Versions versions = fields.getVersions();
        final List<AllowedValue> allowedVersions;
        if (versions != null) {
            allowedVersions = versions.getAllowedValues();
        } else {
            allowedVersions = new ArrayList<>();
        }
        addNoItem(allowedVersions);
        return allowedVersions;
    }

    private List<AllowedValue> getFixVersions(Fields fields) {
        final FixVersions fixVersions = fields.getFixVersions();
        final List<AllowedValue> allowedFixVersions;
        if (fixVersions != null) {
            allowedFixVersions = fixVersions.getAllowedValues();
        } else {
            allowedFixVersions = new ArrayList<>();
        }
        addNoItem(allowedFixVersions);
        return allowedFixVersions;
    }

    private void setIssueTypeSpinnerSelector(final List<IssueType> issueTypes, final AllowedValuesAdapter priorityAdapter, final AllowedValuesAdapter versionsAdapter, final AllowedValuesAdapter fixedInVersionAdapter) {
        issueTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedIssueType = issueTypes.get(position);
                final Fields fields = selectedIssueType.getFields();
                priorityAdapter.setItems(fields.getPriority().getAllowedValues());

                final Versions versions = fields.getVersions();
                final List<AllowedValue> allowedVersions;
                if (versions != null) {
                    allowedVersions = versions.getAllowedValues();
                } else {
                    allowedVersions = new ArrayList<>();
                }
                addNoItem(allowedVersions);
                versionsAdapter.setItems(allowedVersions);

                final List<AllowedValue> allowedFixVersions = getFixVersions(fields);
                fixedInVersionAdapter.setItems(allowedFixVersions);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void filterSubtasks(List<IssueType> issueTypes) {
        for (ListIterator<IssueType> iterator = issueTypes.listIterator(); iterator.hasNext(); ) {
            final IssueType issueType = iterator.next();
            if (issueType.isSubtask()) {
                iterator.remove();
            }
        }
    }

    private void hideProgress() {
        fetchingDataProgress.setVisibility(View.GONE);
    }

    private void showError() {
        hideProgress();
        noProjectData.setVisibility(View.VISIBLE);
    }

    public interface NewIssueCallbacks {
        void onIssueCreated();
    }

    private class ZipModel {
        private final CreateMetaModel metaModel;
        private final List<Assignee> assignees;

        private ZipModel(CreateMetaModel metaModel, List<Assignee> assignees) {
            this.metaModel = metaModel;
            this.assignees = assignees;
        }
    }
}
