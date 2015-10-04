
package com.noiseapps.itassistant.model.jira.issues;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("ALL")
public class Fields {

    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("progress")
    @Expose
    private Progress progress;
    @SerializedName("issuetype")
    @Expose
    private Issuetype issuetype;
    @SerializedName("timespent")
    @Expose
    private long timespent;
    @SerializedName("reporter")
    @Expose
    private Reporter reporter;
    @SerializedName("updated")
    @Expose
    private String updated;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("priority")
    @Expose
    private Priority priority;
    @SerializedName("description")
    @Expose
    private Object description;
    @SerializedName("customfield_10001")
    @Expose
    private Object customfield10001;
    @SerializedName("customfield_10002")
    @Expose
    private String customfield10002;
    @SerializedName("issuelinks")
    @Expose
    private List<Object> issuelinks = new ArrayList<Object>();
    @SerializedName("customfield_10000")
    @Expose
    private List<String> customfield10000 = new ArrayList<String>();
    @SerializedName("subtasks")
    @Expose
    private List<Object> subtasks = new ArrayList<Object>();
    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("customfield_10007")
    @Expose
    private Object customfield10007;
    @SerializedName("customfield_10006")
    @Expose
    private Object customfield10006;
    @SerializedName("labels")
    @Expose
    private List<Object> labels = new ArrayList<Object>();
    @SerializedName("parent")
    @Expose
    private Issue parent;
    @SerializedName("workratio")
    @Expose
    private long workratio;
    @SerializedName("project")
    @Expose
    private Project project;
    @SerializedName("environment")
    @Expose
    private Object environment;
    @SerializedName("customfield_10014")
    @Expose
    private Object customfield10014;
    @SerializedName("aggregateprogress")
    @Expose
    private Aggregateprogress aggregateprogress;
    @SerializedName("lastViewed")
    @Expose
    private Object lastViewed;
    @SerializedName("customfield_10015")
    @Expose
    private Object customfield10015;
    @SerializedName("customfield_10012")
    @Expose
    private Object customfield10012;
    @SerializedName("customfield_10013")
    @Expose
    private Object customfield10013;
    @SerializedName("components")
    @Expose
    private List<Object> components = new ArrayList<Object>();
    @SerializedName("customfield_10010")
    @Expose
    private Object customfield10010;
    @SerializedName("timeoriginalestimate")
    @Expose
    private long timeoriginalestimate;
    @SerializedName("customfield_10011")
    @Expose
    private Object customfield10011;
    @SerializedName("customfield_10017")
    @Expose
    private Object customfield10017;
    @SerializedName("customfield_10016")
    @Expose
    private Object customfield10016;
    @SerializedName("customfield_10019")
    @Expose
    private String customfield10019;
    @SerializedName("customfield_10018")
    @Expose
    private Object customfield10018;
    @SerializedName("votes")
    @Expose
    private Votes votes;
    @SerializedName("fixVersions")
    @Expose
    private List<Object> fixVersions = new ArrayList<Object>();
    @SerializedName("resolution")
    @Expose
    private Resolution resolution;
    @SerializedName("resolutiondate")
    @Expose
    private String resolutiondate;
    @SerializedName("customfield_10211")
    @Expose
    private Object customfield10211;
    @SerializedName("customfield_10210")
    @Expose
    private Object customfield10210;
    @SerializedName("customfield_10212")
    @Expose
    private Object customfield10212;
    @SerializedName("customfield_10203")
    @Expose
    private Object customfield10203;
    @SerializedName("aggregatetimeoriginalestimate")
    @Expose
    private long aggregatetimeoriginalestimate;
    @SerializedName("customfield_10204")
    @Expose
    private Object customfield10204;
    @SerializedName("customfield_10205")
    @Expose
    private Object customfield10205;
    @SerializedName("customfield_10206")
    @Expose
    private Object customfield10206;
    @SerializedName("customfield_10207")
    @Expose
    private Object customfield10207;
    @SerializedName("customfield_10208")
    @Expose
    private Object customfield10208;
    @SerializedName("customfield_10209")
    @Expose
    private Object customfield10209;
    @SerializedName("duedate")
    @Expose
    private String duedate;
    @SerializedName("customfield_10104")
    @Expose
    private Object customfield10104;
    @SerializedName("watches")
    @Expose
    private Watches watches;
    @SerializedName("assignee")
    @Expose
    private Assignee assignee;
    @SerializedName("customfield_10202")
    @Expose
    private Object customfield10202;
    @SerializedName("customfield_10201")
    @Expose
    private Object customfield10201;
    @SerializedName("customfield_10200")
    @Expose
    private Object customfield10200;
    @SerializedName("customfield_10501")
    @Expose
    private Object customfield10501;
    @SerializedName("customfield_10500")
    @Expose
    private Object customfield10500;
    @SerializedName("aggregatetimeestimate")
    @Expose
    private long aggregatetimeestimate;
    @SerializedName("versions")
    @Expose
    private List<Object> versions = new ArrayList<Object>();
    @SerializedName("customfield_10400")
    @Expose
    private String customfield10400;
    @SerializedName("timeestimate")
    @Expose
    private long timeestimate;
    @SerializedName("customfield_10300")
    @Expose
    private Object customfield10300;
    @SerializedName("aggregatetimespent")
    @Expose
    private long aggregatetimespent;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Fields() {
    }

    /**
     * 
     * @param summary
     * @param progress
     * @param issuetype
     * @param customfield10201
     * @param customfield10200
     * @param customfield10203
     * @param customfield10202
     * @param timespent
     * @param customfield10205
     * @param customfield10300
     * @param customfield10204
     * @param reporter
     * @param customfield10206
     * @param customfield10207
     * @param customfield10208
     * @param customfield10209
     * @param customfield10400
     * @param updated
     * @param created
     * @param priority
     * @param description
     * @param issuelinks
     * @param customfield10002
     * @param customfield10001
     * @param subtasks
     * @param customfield10000
     * @param customfield10007
     * @param customfield10006
     * @param status
     * @param labels
     * @param workratio
     * @param parent
     * @param customfield10104
     * @param project
     * @param environment
     * @param aggregateprogress
     * @param lastViewed
     * @param components
     * @param timeoriginalestimate
     * @param customfield10016
     * @param customfield10015
     * @param customfield10018
     * @param customfield10017
     * @param customfield10012
     * @param customfield10011
     * @param customfield10014
     * @param customfield10013
     * @param votes
     * @param customfield10010
     * @param fixVersions
     * @param resolution
     * @param resolutiondate
     * @param aggregatetimeoriginalestimate
     * @param duedate
     * @param customfield10019
     * @param watches
     * @param assignee
     * @param customfield10212
     * @param customfield10501
     * @param customfield10211
     * @param customfield10500
     * @param customfield10210
     * @param aggregatetimeestimate
     * @param versions
     * @param timeestimate
     * @param aggregatetimespent
     */
    public Fields(String summary, Progress progress, Issuetype issuetype, long timespent, Reporter reporter, String updated, String created, Priority priority, Object description, Object customfield10001, String customfield10002, List<Object> issuelinks, List<String> customfield10000, List<Object> subtasks, Status status, Object customfield10007, Object customfield10006, List<Object> labels, Issue parent, long workratio, Project project, Object environment, Object customfield10014, Aggregateprogress aggregateprogress, Object lastViewed, Object customfield10015, Object customfield10012, Object customfield10013, List<Object> components, Object customfield10010, long timeoriginalestimate, Object customfield10011, Object customfield10017, Object customfield10016, String customfield10019, Object customfield10018, Votes votes, List<Object> fixVersions, Resolution resolution, String resolutiondate, Object customfield10211, Object customfield10210, Object customfield10212, Object customfield10203, long aggregatetimeoriginalestimate, Object customfield10204, Object customfield10205, Object customfield10206, Object customfield10207, Object customfield10208, Object customfield10209, String duedate, Object customfield10104, Watches watches, Assignee assignee, Object customfield10202, Object customfield10201, Object customfield10200, Object customfield10501, Object customfield10500, long aggregatetimeestimate, List<Object> versions, String customfield10400, long timeestimate, Object customfield10300, long aggregatetimespent) {
        this.summary = summary;
        this.progress = progress;
        this.issuetype = issuetype;
        this.timespent = timespent;
        this.reporter = reporter;
        this.updated = updated;
        this.created = created;
        this.priority = priority;
        this.description = description;
        this.customfield10001 = customfield10001;
        this.customfield10002 = customfield10002;
        this.issuelinks = issuelinks;
        this.customfield10000 = customfield10000;
        this.subtasks = subtasks;
        this.status = status;
        this.customfield10007 = customfield10007;
        this.customfield10006 = customfield10006;
        this.labels = labels;
        this.parent = parent;
        this.workratio = workratio;
        this.project = project;
        this.environment = environment;
        this.customfield10014 = customfield10014;
        this.aggregateprogress = aggregateprogress;
        this.lastViewed = lastViewed;
        this.customfield10015 = customfield10015;
        this.customfield10012 = customfield10012;
        this.customfield10013 = customfield10013;
        this.components = components;
        this.customfield10010 = customfield10010;
        this.timeoriginalestimate = timeoriginalestimate;
        this.customfield10011 = customfield10011;
        this.customfield10017 = customfield10017;
        this.customfield10016 = customfield10016;
        this.customfield10019 = customfield10019;
        this.customfield10018 = customfield10018;
        this.votes = votes;
        this.fixVersions = fixVersions;
        this.resolution = resolution;
        this.resolutiondate = resolutiondate;
        this.customfield10211 = customfield10211;
        this.customfield10210 = customfield10210;
        this.customfield10212 = customfield10212;
        this.customfield10203 = customfield10203;
        this.aggregatetimeoriginalestimate = aggregatetimeoriginalestimate;
        this.customfield10204 = customfield10204;
        this.customfield10205 = customfield10205;
        this.customfield10206 = customfield10206;
        this.customfield10207 = customfield10207;
        this.customfield10208 = customfield10208;
        this.customfield10209 = customfield10209;
        this.duedate = duedate;
        this.customfield10104 = customfield10104;
        this.watches = watches;
        this.assignee = assignee;
        this.customfield10202 = customfield10202;
        this.customfield10201 = customfield10201;
        this.customfield10200 = customfield10200;
        this.customfield10501 = customfield10501;
        this.customfield10500 = customfield10500;
        this.aggregatetimeestimate = aggregatetimeestimate;
        this.versions = versions;
        this.customfield10400 = customfield10400;
        this.timeestimate = timeestimate;
        this.customfield10300 = customfield10300;
        this.aggregatetimespent = aggregatetimespent;
    }

    /**
     * 
     * @return
     *     The summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * 
     * @param summary
     *     The summary
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * 
     * @return
     *     The progress
     */
    public Progress getProgress() {
        return progress;
    }

    /**
     * 
     * @param progress
     *     The progress
     */
    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    /**
     * 
     * @return
     *     The issuetype
     */
    public Issuetype getIssuetype() {
        return issuetype;
    }

    /**
     * 
     * @param issuetype
     *     The issuetype
     */
    public void setIssuetype(Issuetype issuetype) {
        this.issuetype = issuetype;
    }

    /**
     * 
     * @return
     *     The timespent
     */
    public long getTimespent() {
        return timespent;
    }

    /**
     * 
     * @param timespent
     *     The timespent
     */
    public void setTimespent(long timespent) {
        this.timespent = timespent;
    }

    /**
     * 
     * @return
     *     The reporter
     */
    public Reporter getReporter() {
        return reporter;
    }

    /**
     * 
     * @param reporter
     *     The reporter
     */
    public void setReporter(Reporter reporter) {
        this.reporter = reporter;
    }

    /**
     * 
     * @return
     *     The updated
     */
    public String getUpdated() {
        return updated;
    }

    /**
     * 
     * @param updated
     *     The updated
     */
    public void setUpdated(String updated) {
        this.updated = updated;
    }

    /**
     * 
     * @return
     *     The created
     */
    public String getCreated() {
        return created;
    }

    /**
     * 
     * @param created
     *     The created
     */
    public void setCreated(String created) {
        this.created = created;
    }

    /**
     * 
     * @return
     *     The priority
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * 
     * @param priority
     *     The priority
     */
    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    /**
     * 
     * @return
     *     The description
     */
    public Object getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(Object description) {
        this.description = description;
    }

    /**
     * 
     * @return
     *     The customfield10001
     */
    public Object getCustomfield10001() {
        return customfield10001;
    }

    /**
     * 
     * @param customfield10001
     *     The customfield_10001
     */
    public void setCustomfield10001(Object customfield10001) {
        this.customfield10001 = customfield10001;
    }

    /**
     * 
     * @return
     *     The customfield10002
     */
    public String getCustomfield10002() {
        return customfield10002;
    }

    /**
     * 
     * @param customfield10002
     *     The customfield_10002
     */
    public void setCustomfield10002(String customfield10002) {
        this.customfield10002 = customfield10002;
    }

    /**
     * 
     * @return
     *     The issuelinks
     */
    public List<Object> getIssuelinks() {
        return issuelinks;
    }

    /**
     * 
     * @param issuelinks
     *     The issuelinks
     */
    public void setIssuelinks(List<Object> issuelinks) {
        this.issuelinks = issuelinks;
    }

    /**
     * 
     * @return
     *     The customfield10000
     */
    public List<String> getCustomfield10000() {
        return customfield10000;
    }

    /**
     * 
     * @param customfield10000
     *     The customfield_10000
     */
    public void setCustomfield10000(List<String> customfield10000) {
        this.customfield10000 = customfield10000;
    }

    /**
     * 
     * @return
     *     The subtasks
     */
    public List<Object> getSubtasks() {
        return subtasks;
    }

    /**
     * 
     * @param subtasks
     *     The subtasks
     */
    public void setSubtasks(List<Object> subtasks) {
        this.subtasks = subtasks;
    }

    /**
     * 
     * @return
     *     The status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * 
     * @return
     *     The customfield10007
     */
    public Object getCustomfield10007() {
        return customfield10007;
    }

    /**
     * 
     * @param customfield10007
     *     The customfield_10007
     */
    public void setCustomfield10007(Object customfield10007) {
        this.customfield10007 = customfield10007;
    }

    /**
     * 
     * @return
     *     The customfield10006
     */
    public Object getCustomfield10006() {
        return customfield10006;
    }

    /**
     * 
     * @param customfield10006
     *     The customfield_10006
     */
    public void setCustomfield10006(Object customfield10006) {
        this.customfield10006 = customfield10006;
    }

    /**
     * 
     * @return
     *     The labels
     */
    public List<Object> getLabels() {
        return labels;
    }

    /**
     * 
     * @param labels
     *     The labels
     */
    public void setLabels(List<Object> labels) {
        this.labels = labels;
    }

    /**
     * 
     * @return
     *     The parent
     */
    public Issue getParent() {
        return parent;
    }

    /**
     * 
     * @param parent
     *     The parent
     */
    public void setParent(Issue parent) {
        this.parent = parent;
    }

    /**
     * 
     * @return
     *     The workratio
     */
    public long getWorkratio() {
        return workratio;
    }

    /**
     * 
     * @param workratio
     *     The workratio
     */
    public void setWorkratio(long workratio) {
        this.workratio = workratio;
    }

    /**
     * 
     * @return
     *     The project
     */
    public Project getProject() {
        return project;
    }

    /**
     * 
     * @param project
     *     The project
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * 
     * @return
     *     The environment
     */
    public Object getEnvironment() {
        return environment;
    }

    /**
     * 
     * @param environment
     *     The environment
     */
    public void setEnvironment(Object environment) {
        this.environment = environment;
    }

    /**
     * 
     * @return
     *     The customfield10014
     */
    public Object getCustomfield10014() {
        return customfield10014;
    }

    /**
     * 
     * @param customfield10014
     *     The customfield_10014
     */
    public void setCustomfield10014(Object customfield10014) {
        this.customfield10014 = customfield10014;
    }

    /**
     * 
     * @return
     *     The aggregateprogress
     */
    public Aggregateprogress getAggregateprogress() {
        return aggregateprogress;
    }

    /**
     * 
     * @param aggregateprogress
     *     The aggregateprogress
     */
    public void setAggregateprogress(Aggregateprogress aggregateprogress) {
        this.aggregateprogress = aggregateprogress;
    }

    /**
     * 
     * @return
     *     The lastViewed
     */
    public Object getLastViewed() {
        return lastViewed;
    }

    /**
     * 
     * @param lastViewed
     *     The lastViewed
     */
    public void setLastViewed(Object lastViewed) {
        this.lastViewed = lastViewed;
    }

    /**
     * 
     * @return
     *     The customfield10015
     */
    public Object getCustomfield10015() {
        return customfield10015;
    }

    /**
     * 
     * @param customfield10015
     *     The customfield_10015
     */
    public void setCustomfield10015(Object customfield10015) {
        this.customfield10015 = customfield10015;
    }

    /**
     * 
     * @return
     *     The customfield10012
     */
    public Object getCustomfield10012() {
        return customfield10012;
    }

    /**
     * 
     * @param customfield10012
     *     The customfield_10012
     */
    public void setCustomfield10012(Object customfield10012) {
        this.customfield10012 = customfield10012;
    }

    /**
     * 
     * @return
     *     The customfield10013
     */
    public Object getCustomfield10013() {
        return customfield10013;
    }

    /**
     * 
     * @param customfield10013
     *     The customfield_10013
     */
    public void setCustomfield10013(Object customfield10013) {
        this.customfield10013 = customfield10013;
    }

    /**
     * 
     * @return
     *     The components
     */
    public List<Object> getComponents() {
        return components;
    }

    /**
     * 
     * @param components
     *     The components
     */
    public void setComponents(List<Object> components) {
        this.components = components;
    }

    /**
     * 
     * @return
     *     The customfield10010
     */
    public Object getCustomfield10010() {
        return customfield10010;
    }

    /**
     * 
     * @param customfield10010
     *     The customfield_10010
     */
    public void setCustomfield10010(Object customfield10010) {
        this.customfield10010 = customfield10010;
    }

    /**
     * 
     * @return
     *     The timeoriginalestimate
     */
    public long getTimeoriginalestimate() {
        return timeoriginalestimate;
    }

    /**
     * 
     * @param timeoriginalestimate
     *     The timeoriginalestimate
     */
    public void setTimeoriginalestimate(long timeoriginalestimate) {
        this.timeoriginalestimate = timeoriginalestimate;
    }

    /**
     * 
     * @return
     *     The customfield10011
     */
    public Object getCustomfield10011() {
        return customfield10011;
    }

    /**
     * 
     * @param customfield10011
     *     The customfield_10011
     */
    public void setCustomfield10011(Object customfield10011) {
        this.customfield10011 = customfield10011;
    }

    /**
     * 
     * @return
     *     The customfield10017
     */
    public Object getCustomfield10017() {
        return customfield10017;
    }

    /**
     * 
     * @param customfield10017
     *     The customfield_10017
     */
    public void setCustomfield10017(Object customfield10017) {
        this.customfield10017 = customfield10017;
    }

    /**
     * 
     * @return
     *     The customfield10016
     */
    public Object getCustomfield10016() {
        return customfield10016;
    }

    /**
     * 
     * @param customfield10016
     *     The customfield_10016
     */
    public void setCustomfield10016(Object customfield10016) {
        this.customfield10016 = customfield10016;
    }

    /**
     * 
     * @return
     *     The customfield10019
     */
    public String getCustomfield10019() {
        return customfield10019;
    }

    /**
     * 
     * @param customfield10019
     *     The customfield_10019
     */
    public void setCustomfield10019(String customfield10019) {
        this.customfield10019 = customfield10019;
    }

    /**
     * 
     * @return
     *     The customfield10018
     */
    public Object getCustomfield10018() {
        return customfield10018;
    }

    /**
     * 
     * @param customfield10018
     *     The customfield_10018
     */
    public void setCustomfield10018(Object customfield10018) {
        this.customfield10018 = customfield10018;
    }

    /**
     * 
     * @return
     *     The votes
     */
    public Votes getVotes() {
        return votes;
    }

    /**
     * 
     * @param votes
     *     The votes
     */
    public void setVotes(Votes votes) {
        this.votes = votes;
    }

    /**
     * 
     * @return
     *     The fixVersions
     */
    public List<Object> getFixVersions() {
        return fixVersions;
    }

    /**
     * 
     * @param fixVersions
     *     The fixVersions
     */
    public void setFixVersions(List<Object> fixVersions) {
        this.fixVersions = fixVersions;
    }

    /**
     * 
     * @return
     *     The resolution
     */
    public Resolution getResolution() {
        return resolution;
    }

    /**
     * 
     * @param resolution
     *     The resolution
     */
    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }

    /**
     * 
     * @return
     *     The resolutiondate
     */
    public String getResolutiondate() {
        return resolutiondate;
    }

    /**
     * 
     * @param resolutiondate
     *     The resolutiondate
     */
    public void setResolutiondate(String resolutiondate) {
        this.resolutiondate = resolutiondate;
    }

    /**
     * 
     * @return
     *     The customfield10211
     */
    public Object getCustomfield10211() {
        return customfield10211;
    }

    /**
     * 
     * @param customfield10211
     *     The customfield_10211
     */
    public void setCustomfield10211(Object customfield10211) {
        this.customfield10211 = customfield10211;
    }

    /**
     * 
     * @return
     *     The customfield10210
     */
    public Object getCustomfield10210() {
        return customfield10210;
    }

    /**
     * 
     * @param customfield10210
     *     The customfield_10210
     */
    public void setCustomfield10210(Object customfield10210) {
        this.customfield10210 = customfield10210;
    }

    /**
     * 
     * @return
     *     The customfield10212
     */
    public Object getCustomfield10212() {
        return customfield10212;
    }

    /**
     * 
     * @param customfield10212
     *     The customfield_10212
     */
    public void setCustomfield10212(Object customfield10212) {
        this.customfield10212 = customfield10212;
    }

    /**
     * 
     * @return
     *     The customfield10203
     */
    public Object getCustomfield10203() {
        return customfield10203;
    }

    /**
     * 
     * @param customfield10203
     *     The customfield_10203
     */
    public void setCustomfield10203(Object customfield10203) {
        this.customfield10203 = customfield10203;
    }

    /**
     * 
     * @return
     *     The aggregatetimeoriginalestimate
     */
    public long getAggregatetimeoriginalestimate() {
        return aggregatetimeoriginalestimate;
    }

    /**
     * 
     * @param aggregatetimeoriginalestimate
     *     The aggregatetimeoriginalestimate
     */
    public void setAggregatetimeoriginalestimate(long aggregatetimeoriginalestimate) {
        this.aggregatetimeoriginalestimate = aggregatetimeoriginalestimate;
    }

    /**
     * 
     * @return
     *     The customfield10204
     */
    public Object getCustomfield10204() {
        return customfield10204;
    }

    /**
     * 
     * @param customfield10204
     *     The customfield_10204
     */
    public void setCustomfield10204(Object customfield10204) {
        this.customfield10204 = customfield10204;
    }

    /**
     * 
     * @return
     *     The customfield10205
     */
    public Object getCustomfield10205() {
        return customfield10205;
    }

    /**
     * 
     * @param customfield10205
     *     The customfield_10205
     */
    public void setCustomfield10205(Object customfield10205) {
        this.customfield10205 = customfield10205;
    }

    /**
     * 
     * @return
     *     The customfield10206
     */
    public Object getCustomfield10206() {
        return customfield10206;
    }

    /**
     * 
     * @param customfield10206
     *     The customfield_10206
     */
    public void setCustomfield10206(Object customfield10206) {
        this.customfield10206 = customfield10206;
    }

    /**
     * 
     * @return
     *     The customfield10207
     */
    public Object getCustomfield10207() {
        return customfield10207;
    }

    /**
     * 
     * @param customfield10207
     *     The customfield_10207
     */
    public void setCustomfield10207(Object customfield10207) {
        this.customfield10207 = customfield10207;
    }

    /**
     * 
     * @return
     *     The customfield10208
     */
    public Object getCustomfield10208() {
        return customfield10208;
    }

    /**
     * 
     * @param customfield10208
     *     The customfield_10208
     */
    public void setCustomfield10208(Object customfield10208) {
        this.customfield10208 = customfield10208;
    }

    /**
     * 
     * @return
     *     The customfield10209
     */
    public Object getCustomfield10209() {
        return customfield10209;
    }

    /**
     * 
     * @param customfield10209
     *     The customfield_10209
     */
    public void setCustomfield10209(Object customfield10209) {
        this.customfield10209 = customfield10209;
    }

    /**
     * 
     * @return
     *     The duedate
     */
    public String getDuedate() {
        return duedate;
    }

    /**
     * 
     * @param duedate
     *     The duedate
     */
    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }

    /**
     * 
     * @return
     *     The customfield10104
     */
    public Object getCustomfield10104() {
        return customfield10104;
    }

    /**
     * 
     * @param customfield10104
     *     The customfield_10104
     */
    public void setCustomfield10104(Object customfield10104) {
        this.customfield10104 = customfield10104;
    }

    /**
     * 
     * @return
     *     The watches
     */
    public Watches getWatches() {
        return watches;
    }

    /**
     * 
     * @param watches
     *     The watches
     */
    public void setWatches(Watches watches) {
        this.watches = watches;
    }

    /**
     * 
     * @return
     *     The assignee
     */
    public Assignee getAssignee() {
        return assignee;
    }

    /**
     * 
     * @param assignee
     *     The assignee
     */
    public void setAssignee(Assignee assignee) {
        this.assignee = assignee;
    }

    /**
     * 
     * @return
     *     The customfield10202
     */
    public Object getCustomfield10202() {
        return customfield10202;
    }

    /**
     * 
     * @param customfield10202
     *     The customfield_10202
     */
    public void setCustomfield10202(Object customfield10202) {
        this.customfield10202 = customfield10202;
    }

    /**
     * 
     * @return
     *     The customfield10201
     */
    public Object getCustomfield10201() {
        return customfield10201;
    }

    /**
     * 
     * @param customfield10201
     *     The customfield_10201
     */
    public void setCustomfield10201(Object customfield10201) {
        this.customfield10201 = customfield10201;
    }

    /**
     * 
     * @return
     *     The customfield10200
     */
    public Object getCustomfield10200() {
        return customfield10200;
    }

    /**
     * 
     * @param customfield10200
     *     The customfield_10200
     */
    public void setCustomfield10200(Object customfield10200) {
        this.customfield10200 = customfield10200;
    }

    /**
     * 
     * @return
     *     The customfield10501
     */
    public Object getCustomfield10501() {
        return customfield10501;
    }

    /**
     * 
     * @param customfield10501
     *     The customfield_10501
     */
    public void setCustomfield10501(Object customfield10501) {
        this.customfield10501 = customfield10501;
    }

    /**
     * 
     * @return
     *     The customfield10500
     */
    public Object getCustomfield10500() {
        return customfield10500;
    }

    /**
     * 
     * @param customfield10500
     *     The customfield_10500
     */
    public void setCustomfield10500(Object customfield10500) {
        this.customfield10500 = customfield10500;
    }

    /**
     * 
     * @return
     *     The aggregatetimeestimate
     */
    public long getAggregatetimeestimate() {
        return aggregatetimeestimate;
    }

    /**
     * 
     * @param aggregatetimeestimate
     *     The aggregatetimeestimate
     */
    public void setAggregatetimeestimate(long aggregatetimeestimate) {
        this.aggregatetimeestimate = aggregatetimeestimate;
    }

    /**
     * 
     * @return
     *     The versions
     */
    public List<Object> getVersions() {
        return versions;
    }

    /**
     * 
     * @param versions
     *     The versions
     */
    public void setVersions(List<Object> versions) {
        this.versions = versions;
    }

    /**
     * 
     * @return
     *     The customfield10400
     */
    public String getCustomfield10400() {
        return customfield10400;
    }

    /**
     * 
     * @param customfield10400
     *     The customfield_10400
     */
    public void setCustomfield10400(String customfield10400) {
        this.customfield10400 = customfield10400;
    }

    /**
     * 
     * @return
     *     The timeestimate
     */
    public long getTimeestimate() {
        return timeestimate;
    }

    /**
     * 
     * @param timeestimate
     *     The timeestimate
     */
    public void setTimeestimate(long timeestimate) {
        this.timeestimate = timeestimate;
    }

    /**
     * 
     * @return
     *     The customfield10300
     */
    public Object getCustomfield10300() {
        return customfield10300;
    }

    /**
     * 
     * @param customfield10300
     *     The customfield_10300
     */
    public void setCustomfield10300(Object customfield10300) {
        this.customfield10300 = customfield10300;
    }

    /**
     * 
     * @return
     *     The aggregatetimespent
     */
    public long getAggregatetimespent() {
        return aggregatetimespent;
    }

    /**
     * 
     * @param aggregatetimespent
     *     The aggregatetimespent
     */
    public void setAggregatetimespent(long aggregatetimespent) {
        this.aggregatetimespent = aggregatetimespent;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(summary).append(progress).append(issuetype).append(timespent).append(reporter).append(updated).append(created).append(priority).append(description).append(customfield10001).append(customfield10002).append(issuelinks).append(customfield10000).append(subtasks).append(status).append(customfield10007).append(customfield10006).append(labels).append(parent).append(workratio).append(project).append(environment).append(customfield10014).append(aggregateprogress).append(lastViewed).append(customfield10015).append(customfield10012).append(customfield10013).append(components).append(customfield10010).append(timeoriginalestimate).append(customfield10011).append(customfield10017).append(customfield10016).append(customfield10019).append(customfield10018).append(votes).append(fixVersions).append(resolution).append(resolutiondate).append(customfield10211).append(customfield10210).append(customfield10212).append(customfield10203).append(aggregatetimeoriginalestimate).append(customfield10204).append(customfield10205).append(customfield10206).append(customfield10207).append(customfield10208).append(customfield10209).append(duedate).append(customfield10104).append(watches).append(assignee).append(customfield10202).append(customfield10201).append(customfield10200).append(customfield10501).append(customfield10500).append(aggregatetimeestimate).append(versions).append(customfield10400).append(timeestimate).append(customfield10300).append(aggregatetimespent).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Fields) == false) {
            return false;
        }
        Fields rhs = ((Fields) other);
        return new EqualsBuilder().append(summary, rhs.summary).append(progress, rhs.progress).append(issuetype, rhs.issuetype).append(timespent, rhs.timespent).append(reporter, rhs.reporter).append(updated, rhs.updated).append(created, rhs.created).append(priority, rhs.priority).append(description, rhs.description).append(customfield10001, rhs.customfield10001).append(customfield10002, rhs.customfield10002).append(issuelinks, rhs.issuelinks).append(customfield10000, rhs.customfield10000).append(subtasks, rhs.subtasks).append(status, rhs.status).append(customfield10007, rhs.customfield10007).append(customfield10006, rhs.customfield10006).append(labels, rhs.labels).append(parent, rhs.parent).append(workratio, rhs.workratio).append(project, rhs.project).append(environment, rhs.environment).append(customfield10014, rhs.customfield10014).append(aggregateprogress, rhs.aggregateprogress).append(lastViewed, rhs.lastViewed).append(customfield10015, rhs.customfield10015).append(customfield10012, rhs.customfield10012).append(customfield10013, rhs.customfield10013).append(components, rhs.components).append(customfield10010, rhs.customfield10010).append(timeoriginalestimate, rhs.timeoriginalestimate).append(customfield10011, rhs.customfield10011).append(customfield10017, rhs.customfield10017).append(customfield10016, rhs.customfield10016).append(customfield10019, rhs.customfield10019).append(customfield10018, rhs.customfield10018).append(votes, rhs.votes).append(fixVersions, rhs.fixVersions).append(resolution, rhs.resolution).append(resolutiondate, rhs.resolutiondate).append(customfield10211, rhs.customfield10211).append(customfield10210, rhs.customfield10210).append(customfield10212, rhs.customfield10212).append(customfield10203, rhs.customfield10203).append(aggregatetimeoriginalestimate, rhs.aggregatetimeoriginalestimate).append(customfield10204, rhs.customfield10204).append(customfield10205, rhs.customfield10205).append(customfield10206, rhs.customfield10206).append(customfield10207, rhs.customfield10207).append(customfield10208, rhs.customfield10208).append(customfield10209, rhs.customfield10209).append(duedate, rhs.duedate).append(customfield10104, rhs.customfield10104).append(watches, rhs.watches).append(assignee, rhs.assignee).append(customfield10202, rhs.customfield10202).append(customfield10201, rhs.customfield10201).append(customfield10200, rhs.customfield10200).append(customfield10501, rhs.customfield10501).append(customfield10500, rhs.customfield10500).append(aggregatetimeestimate, rhs.aggregatetimeestimate).append(versions, rhs.versions).append(customfield10400, rhs.customfield10400).append(timeestimate, rhs.timeestimate).append(customfield10300, rhs.customfield10300).append(aggregatetimespent, rhs.aggregatetimespent).isEquals();
    }

}
