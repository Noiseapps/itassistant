package com.noiseapps.itassistant.model.jira.issues;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.noiseapps.itassistant.model.jira.issues.common.IssueType;


@SuppressWarnings("ALL")
public class Fields implements Parcelable {

    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("progress")
    @Expose
    private Progress progress;
    @SerializedName("issuetype")
    @Expose
    private IssueType issueType;
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
    private String description;

    @SerializedName("issuelinks")
    @Expose
    private List<String> issuelinks = new ArrayList<String>();
    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("labels")
    @Expose
    private List<String> labels = new ArrayList<String>();
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
    private String environment;
    @SerializedName("aggregateprogress")
    @Expose
    private AggregateProgress aggregateProgress;
    @SerializedName("components")
    @Expose
    private List<Component> components = new ArrayList<Component>();
    @SerializedName("votes")
    @Expose
    private Votes votes;
    @SerializedName("resolution")
    @Expose
    private Resolution resolution;
    @SerializedName("resolutiondate")
    @Expose
    private String resolutiondate;
    @SerializedName("aggregatetimeoriginalestimate")
    @Expose
    private long aggregatetimeoriginalestimate;
    @SerializedName("duedate")
    @Expose
    private String duedate;
    @SerializedName("watches")
    @Expose
    private Watches watches;
    @SerializedName("assignee")
    @Expose
    private Assignee assignee;
    @SerializedName("aggregatetimeestimate")
    @Expose
    private long aggregatetimeestimate;
    @SerializedName("timeestimate")
    @Expose
    private long timeestimate;
    @SerializedName("aggregatetimespent")
    @Expose
    private long aggregatetimespent;

    public List<FixVersion> getFixVersions() {
        return fixVersions;
    }

    public void setFixVersions(List<FixVersion> fixVersions) {
        this.fixVersions = fixVersions;
    }

    @Override
    public String toString() {
        return "Fields{" +
                "summary='" + summary + '\'' +
                ", progress=" + progress +
                ", issueType=" + issueType +
                ", timespent=" + timespent +
                ", reporter=" + reporter +
                ", updated='" + updated + '\'' +
                ", created='" + created + '\'' +
                ", priority=" + priority +
                ", description='" + description + '\'' +
                ", issuelinks=" + issuelinks +
                ", status=" + status +
                ", labels=" + labels +
                ", parent=" + parent +
                ", workratio=" + workratio +
                ", project=" + project +
                ", environment='" + environment + '\'' +
                ", aggregateProgress=" + aggregateProgress +
                ", components=" + components +
                ", votes=" + votes +
                ", resolution=" + resolution +
                ", resolutiondate='" + resolutiondate + '\'' +
                ", aggregatetimeoriginalestimate=" + aggregatetimeoriginalestimate +
                ", duedate='" + duedate + '\'' +
                ", watches=" + watches +
                ", assignee=" + assignee +
                ", aggregatetimeestimate=" + aggregatetimeestimate +
                ", timeestimate=" + timeestimate +
                ", aggregatetimespent=" + aggregatetimespent +
                ", fixVersions=" + fixVersions +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fields fields = (Fields) o;

        if (timespent != fields.timespent) return false;
        if (workratio != fields.workratio) return false;
        if (aggregatetimeoriginalestimate != fields.aggregatetimeoriginalestimate) return false;
        if (aggregatetimeestimate != fields.aggregatetimeestimate) return false;
        if (timeestimate != fields.timeestimate) return false;
        if (aggregatetimespent != fields.aggregatetimespent) return false;
        if (summary != null ? !summary.equals(fields.summary) : fields.summary != null)
            return false;
        if (progress != null ? !progress.equals(fields.progress) : fields.progress != null)
            return false;
        if (issueType != null ? !issueType.equals(fields.issueType) : fields.issueType != null)
            return false;
        if (reporter != null ? !reporter.equals(fields.reporter) : fields.reporter != null)
            return false;
        if (updated != null ? !updated.equals(fields.updated) : fields.updated != null)
            return false;
        if (created != null ? !created.equals(fields.created) : fields.created != null)
            return false;
        if (priority != null ? !priority.equals(fields.priority) : fields.priority != null)
            return false;
        if (description != null ? !description.equals(fields.description) : fields.description != null)
            return false;
        if (issuelinks != null ? !issuelinks.equals(fields.issuelinks) : fields.issuelinks != null)
            return false;
        if (status != null ? !status.equals(fields.status) : fields.status != null) return false;
        if (labels != null ? !labels.equals(fields.labels) : fields.labels != null) return false;
        if (parent != null ? !parent.equals(fields.parent) : fields.parent != null) return false;
        if (project != null ? !project.equals(fields.project) : fields.project != null)
            return false;
        if (environment != null ? !environment.equals(fields.environment) : fields.environment != null)
            return false;
        if (aggregateProgress != null ? !aggregateProgress.equals(fields.aggregateProgress) : fields.aggregateProgress != null)
            return false;
        if (components != null ? !components.equals(fields.components) : fields.components != null)
            return false;
        if (votes != null ? !votes.equals(fields.votes) : fields.votes != null) return false;
        if (resolution != null ? !resolution.equals(fields.resolution) : fields.resolution != null)
            return false;
        if (resolutiondate != null ? !resolutiondate.equals(fields.resolutiondate) : fields.resolutiondate != null)
            return false;
        if (duedate != null ? !duedate.equals(fields.duedate) : fields.duedate != null)
            return false;
        if (watches != null ? !watches.equals(fields.watches) : fields.watches != null)
            return false;
        if (assignee != null ? !assignee.equals(fields.assignee) : fields.assignee != null)
            return false;
        if (fixVersions != null ? !fixVersions.equals(fields.fixVersions) : fields.fixVersions != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = summary != null ? summary.hashCode() : 0;
        result = 31 * result + (progress != null ? progress.hashCode() : 0);
        result = 31 * result + (issueType != null ? issueType.hashCode() : 0);
        result = 31 * result + (int) (timespent ^ (timespent >>> 32));
        result = 31 * result + (reporter != null ? reporter.hashCode() : 0);
        result = 31 * result + (updated != null ? updated.hashCode() : 0);
        result = 31 * result + (created != null ? created.hashCode() : 0);
        result = 31 * result + (priority != null ? priority.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (issuelinks != null ? issuelinks.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (labels != null ? labels.hashCode() : 0);
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        result = 31 * result + (int) (workratio ^ (workratio >>> 32));
        result = 31 * result + (project != null ? project.hashCode() : 0);
        result = 31 * result + (environment != null ? environment.hashCode() : 0);
        result = 31 * result + (aggregateProgress != null ? aggregateProgress.hashCode() : 0);
        result = 31 * result + (components != null ? components.hashCode() : 0);
        result = 31 * result + (votes != null ? votes.hashCode() : 0);
        result = 31 * result + (resolution != null ? resolution.hashCode() : 0);
        result = 31 * result + (resolutiondate != null ? resolutiondate.hashCode() : 0);
        result = 31 * result + (int) (aggregatetimeoriginalestimate ^ (aggregatetimeoriginalestimate >>> 32));
        result = 31 * result + (duedate != null ? duedate.hashCode() : 0);
        result = 31 * result + (watches != null ? watches.hashCode() : 0);
        result = 31 * result + (assignee != null ? assignee.hashCode() : 0);
        result = 31 * result + (int) (aggregatetimeestimate ^ (aggregatetimeestimate >>> 32));
        result = 31 * result + (int) (timeestimate ^ (timeestimate >>> 32));
        result = 31 * result + (int) (aggregatetimespent ^ (aggregatetimespent >>> 32));
        result = 31 * result + (fixVersions != null ? fixVersions.hashCode() : 0);
        return result;
    }

    private List<FixVersion> fixVersions = new ArrayList<FixVersion>();

    /**
     * No args constructor for use in serialization
     */
    public Fields() {
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    public IssueType getIssueType() {
        return issueType;
    }

    public void setIssueType(IssueType issueType) {
        this.issueType = issueType;
    }

    public long getTimespent() {
        return timespent;
    }

    public void setTimespent(long timespent) {
        this.timespent = timespent;
    }

    public Reporter getReporter() {
        return reporter;
    }

    public void setReporter(Reporter reporter) {
        this.reporter = reporter;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getIssuelinks() {
        return issuelinks;
    }

    public void setIssuelinks(List<String> issuelinks) {
        this.issuelinks = issuelinks;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public Issue getParent() {
        return parent;
    }

    public void setParent(Issue parent) {
        this.parent = parent;
    }

    public long getWorkratio() {
        return workratio;
    }

    public void setWorkratio(long workratio) {
        this.workratio = workratio;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public AggregateProgress getAggregateProgress() {
        return aggregateProgress;
    }

    public void setAggregateProgress(AggregateProgress aggregateProgress) {
        this.aggregateProgress = aggregateProgress;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public Votes getVotes() {
        return votes;
    }

    public void setVotes(Votes votes) {
        this.votes = votes;
    }

    public Resolution getResolution() {
        return resolution;
    }

    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }

    public String getResolutiondate() {
        return resolutiondate;
    }

    public void setResolutiondate(String resolutiondate) {
        this.resolutiondate = resolutiondate;
    }

    public long getAggregatetimeoriginalestimate() {
        return aggregatetimeoriginalestimate;
    }

    public void setAggregatetimeoriginalestimate(long aggregatetimeoriginalestimate) {
        this.aggregatetimeoriginalestimate = aggregatetimeoriginalestimate;
    }

    public String getDuedate() {
        return duedate;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }

    public Watches getWatches() {
        return watches;
    }

    public void setWatches(Watches watches) {
        this.watches = watches;
    }

    public Assignee getAssignee() {
        return assignee;
    }

    public void setAssignee(Assignee assignee) {
        this.assignee = assignee;
    }

    public long getAggregatetimeestimate() {
        return aggregatetimeestimate;
    }

    public void setAggregatetimeestimate(long aggregatetimeestimate) {
        this.aggregatetimeestimate = aggregatetimeestimate;
    }

    public long getTimeestimate() {
        return timeestimate;
    }

    public void setTimeestimate(long timeestimate) {
        this.timeestimate = timeestimate;
    }

    public long getAggregatetimespent() {
        return aggregatetimespent;
    }

    public void setAggregatetimespent(long aggregatetimespent) {
        this.aggregatetimespent = aggregatetimespent;
    }

    public static Creator<Fields> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.summary);
        dest.writeParcelable(this.progress, 0);
        dest.writeParcelable(this.issueType, 0);
        dest.writeLong(this.timespent);
        dest.writeParcelable(this.reporter, 0);
        dest.writeString(this.updated);
        dest.writeString(this.created);
        dest.writeParcelable(this.priority, 0);
        dest.writeString(this.description);
        dest.writeStringList(this.issuelinks);
        dest.writeParcelable(this.status, 0);
        dest.writeStringList(this.labels);
        dest.writeParcelable(this.parent, 0);
        dest.writeLong(this.workratio);
        dest.writeParcelable(this.project, 0);
        dest.writeString(this.environment);
        dest.writeParcelable(this.aggregateProgress, 0);
        dest.writeTypedList(components);
        dest.writeTypedList(fixVersions);
        dest.writeParcelable(this.votes, 0);
        dest.writeParcelable(this.resolution, 0);
        dest.writeString(this.resolutiondate);
        dest.writeLong(this.aggregatetimeoriginalestimate);
        dest.writeString(this.duedate);
        dest.writeParcelable(this.watches, 0);
        dest.writeParcelable(this.assignee, 0);
        dest.writeLong(this.aggregatetimeestimate);
        dest.writeLong(this.timeestimate);
        dest.writeLong(this.aggregatetimespent);
    }

    protected Fields(Parcel in) {
        this.summary = in.readString();
        this.progress = in.readParcelable(Progress.class.getClassLoader());
        this.issueType = in.readParcelable(IssueType.class.getClassLoader());
        this.timespent = in.readLong();
        this.reporter = in.readParcelable(Reporter.class.getClassLoader());
        this.updated = in.readString();
        this.created = in.readString();
        this.priority = in.readParcelable(Priority.class.getClassLoader());
        this.description = in.readString();
        this.issuelinks = in.createStringArrayList();
        this.status = in.readParcelable(Status.class.getClassLoader());
        this.labels = in.createStringArrayList();
        this.parent = in.readParcelable(Issue.class.getClassLoader());
        this.workratio = in.readLong();
        this.project = in.readParcelable(Project.class.getClassLoader());
        this.environment = in.readString();
        this.aggregateProgress = in.readParcelable(AggregateProgress.class.getClassLoader());
        this.components = in.createTypedArrayList(Component.CREATOR);
        this.fixVersions = in.createTypedArrayList(FixVersion.CREATOR);
        this.votes = in.readParcelable(Votes.class.getClassLoader());
        this.resolution = in.readParcelable(Resolution.class.getClassLoader());
        this.resolutiondate = in.readString();
        this.aggregatetimeoriginalestimate = in.readLong();
        this.duedate = in.readString();
        this.watches = in.readParcelable(Watches.class.getClassLoader());
        this.assignee = in.readParcelable(Assignee.class.getClassLoader());
        this.aggregatetimeestimate = in.readLong();
        this.timeestimate = in.readLong();
        this.aggregatetimespent = in.readLong();
    }

    public static final Creator<Fields> CREATOR = new Creator<Fields>() {
        public Fields createFromParcel(Parcel source) {
            return new Fields(source);
        }

        public Fields[] newArray(int size) {
            return new Fields[size];
        }
    };
}