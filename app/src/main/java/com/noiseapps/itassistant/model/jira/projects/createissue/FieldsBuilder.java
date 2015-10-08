package com.noiseapps.itassistant.model.jira.projects.createissue;

public class FieldsBuilder {
    private CreateIssueModel.Fields.IdField project;
    private CreateIssueModel.Fields.IdField issuetype;
    private CreateIssueModel.Fields.IdField priority;
    private CreateIssueModel.Fields.NameField assignee;
    private CreateIssueModel.Fields.NameField reporter;
    private String summary;
    private String description;
    private String environment;
    private String duedate;

    public FieldsBuilder setProject(CreateIssueModel.Fields.IdField project) {
        this.project = project;
        return this;
    }

    public FieldsBuilder setIssuetype(CreateIssueModel.Fields.IdField issuetype) {
        this.issuetype = issuetype;
        return this;
    }

    public FieldsBuilder setPriority(CreateIssueModel.Fields.IdField priority) {
        this.priority = priority;
        return this;
    }

    public FieldsBuilder setAssignee(CreateIssueModel.Fields.NameField assignee) {
        this.assignee = assignee;
        return this;
    }

    public FieldsBuilder setReporter(CreateIssueModel.Fields.NameField reporter) {
        this.reporter = reporter;
        return this;
    }

    public FieldsBuilder setSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public FieldsBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public FieldsBuilder setEnvironment(String environment) {
        this.environment = environment;
        return this;
    }

    public FieldsBuilder setDuedate(String duedate) {
        this.duedate = duedate;
        return this;
    }

    public CreateIssueModel.Fields createFields() {
        return new CreateIssueModel.Fields(project, issuetype, priority, assignee, reporter, summary, description, environment, duedate);
    }
}