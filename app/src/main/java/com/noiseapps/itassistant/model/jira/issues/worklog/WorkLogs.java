package com.noiseapps.itassistant.model.jira.issues.worklog;

import java.util.List;

public class WorkLogs {

    long startAt;
    long maxResults;
    long total;
    List<WorkLogItem> worklogs;

    public List<WorkLogItem> getWorklogs() {
        return worklogs;
    }

    public long getStartAt() {
        return startAt;
    }

    public long getMaxResults() {
        return maxResults;
    }

    public long getTotal() {
        return total;
    }
}
