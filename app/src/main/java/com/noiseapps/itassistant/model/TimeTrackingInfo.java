package com.noiseapps.itassistant.model;

import com.noiseapps.itassistant.model.jira.issues.Issue;

public class TimeTrackingInfo {

    private final Issue issue;
    private final long started;

    public TimeTrackingInfo(Issue issue, long started) {
        this.issue = issue;
        this.started = started;
    }

    public Issue getIssue() {
        return issue;
    }

    public long getStarted() {
        return started;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimeTrackingInfo that = (TimeTrackingInfo) o;

        if (started != that.started) return false;
        return !(issue != null ? !issue.equals(that.issue) : that.issue != null);

    }

    @Override
    public int hashCode() {
        int result = issue != null ? issue.hashCode() : 0;
        result = 31 * result + (int) (started ^ (started >>> 32));
        return result;
    }
}
