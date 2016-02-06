package com.noiseapps.itassistant.utils;

import java.util.Comparator;

import com.noiseapps.itassistant.model.jira.issues.Issue;

public class Comparators {

    public static class ISSUE {
        public static final Comparator<Issue> BY_KEY = (lhs, rhs) -> lhs.getKey().compareTo(rhs.getKey());
        public static final Comparator<Issue> BY_TYPE = (l, r) -> l.getFields().getIssueType().getId().compareTo(r.getFields().getIssueType().getId());
        public static final Comparator<Issue> BY_PRIORITY = (l, r) -> l.getFields().getPriority().getId().compareTo(r.getFields().getPriority().getId());
        public static final Comparator<Issue> BY_SUMMARY = (l, r) -> l.getFields().getSummary().compareTo(r.getFields().getSummary());
        public static final Comparator<Issue> BY_ASSIGNEE = ISSUE::compareByAssignee;
        public static final Comparator<Issue> BY_MODIFIED = (l, r) -> l.getFields().getUpdated().compareTo(r.getFields().getUpdated());
        public static final Comparator<Issue> BY_CREATED = (l, r) -> l.getFields().getCreated().compareTo(r.getFields().getCreated());


        private static int compareByAssignee(Issue l, Issue r) {
            String lAssignee = l.getFields().getAssignee() == null ? "" : l.getFields().getAssignee().getName();
            String rAssignee = r.getFields().getAssignee() == null ? "" : r.getFields().getAssignee().getName();
            return lAssignee.compareTo(rAssignee);
        }
    }
}
