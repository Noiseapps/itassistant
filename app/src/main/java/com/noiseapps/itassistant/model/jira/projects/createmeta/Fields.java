package com.noiseapps.itassistant.model.jira.projects.createmeta;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Fields {

    @SerializedName("summary")
    @Expose
    private Summary summary;
    @SerializedName("timetracking")
    @Expose
    private Timetracking timetracking;
    @SerializedName("issueType")
    @Expose
    private IssueType issueType;
    @SerializedName("labels")
    @Expose
    private Labels labels;
    @SerializedName("assignee")
    @Expose
    private Assignee assignee;
    @SerializedName("fixVersions")
    @Expose
    private FixVersions fixVersions;
    @SerializedName("attachment")
    @Expose
    private Attachment attachment;
    @SerializedName("project")
    @Expose
    private Project project;
    @SerializedName("versions")
    @Expose
    private Versions versions;
    @SerializedName("environment")
    @Expose
    private Environment environment;
    @SerializedName("priority")
    @Expose
    private Priority priority;
    @SerializedName("description")
    @Expose
    private Description description;
    @SerializedName("customfield10001")
    @Expose
    private Customfield10001 customfield10001;
    @SerializedName("duedate")
    @Expose
    private Duedate duedate;
    @SerializedName("components")
    @Expose
    private Components components;
    @SerializedName("customfield10000")
    @Expose
    private Customfield10000 customfield10000;

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public Timetracking getTimetracking() {
        return timetracking;
    }

    public void setTimetracking(Timetracking timetracking) {
        this.timetracking = timetracking;
    }

    public IssueType getIssueType() {
        return issueType;
    }

    public void setIssueType(IssueType issueType) {
        this.issueType = issueType;
    }

    public Labels getLabels() {
        return labels;
    }

    public void setLabels(Labels labels) {
        this.labels = labels;
    }

    public Assignee getAssignee() {
        return assignee;
    }

    public void setAssignee(Assignee assignee) {
        this.assignee = assignee;
    }

    public FixVersions getFixVersions() {
        return fixVersions;
    }

    public void setFixVersions(FixVersions fixVersions) {
        this.fixVersions = fixVersions;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Versions getVersions() {
        return versions;
    }

    public void setVersions(Versions versions) {
        this.versions = versions;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public Customfield10001 getCustomfield10001() {
        return customfield10001;
    }

    public void setCustomfield10001(Customfield10001 customfield10001) {
        this.customfield10001 = customfield10001;
    }

    public Duedate getDuedate() {
        return duedate;
    }

    public void setDuedate(Duedate duedate) {
        this.duedate = duedate;
    }

    public Components getComponents() {
        return components;
    }

    public void setComponents(Components components) {
        this.components = components;
    }

    public Customfield10000 getCustomfield10000() {
        return customfield10000;
    }

    public void setCustomfield10000(Customfield10000 customfield10000) {
        this.customfield10000 = customfield10000;
    }
}
