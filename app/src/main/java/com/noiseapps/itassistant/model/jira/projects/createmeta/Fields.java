
package com.noiseapps.itassistant.model.jira.projects.createmeta;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Fields {

    @SerializedName("summary")
    @Expose
    public Summary summary;
    @SerializedName("timetracking")
    @Expose
    public Timetracking timetracking;
    @SerializedName("issuetype")
    @Expose
    public Issuetype_ issuetype;
    @SerializedName("labels")
    @Expose
    public Labels labels;
    @SerializedName("assignee")
    @Expose
    public Assignee assignee;
    @SerializedName("fixVersions")
    @Expose
    public FixVersions fixVersions;
    @SerializedName("attachment")
    @Expose
    public Attachment attachment;
    @SerializedName("project")
    @Expose
    public Project_ project;
    @SerializedName("versions")
    @Expose
    public Versions versions;
    @SerializedName("environment")
    @Expose
    public Environment environment;
    @SerializedName("priority")
    @Expose
    public Priority priority;
    @SerializedName("description")
    @Expose
    public Description description;
    @SerializedName("customfield_10001")
    @Expose
    public Customfield10001 customfield10001;
    @SerializedName("duedate")
    @Expose
    public Duedate duedate;
    @SerializedName("components")
    @Expose
    public Components components;
    @SerializedName("customfield_10000")
    @Expose
    public Customfield10000 customfield10000;

}
