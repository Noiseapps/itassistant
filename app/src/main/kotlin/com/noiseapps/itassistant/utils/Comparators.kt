package com.noiseapps.itassistant.utils

import com.noiseapps.itassistant.model.jira.issues.Issue
import java.util.*

public class Comparators {

    public object ISSUE {
        @JvmField public val BY_KEY: Comparator<Issue> = Comparator { l, r -> l.key.compareTo(r.key) }
        @JvmField public val BY_TYPE: Comparator<Issue> = Comparator { l, r -> l.fields.issueType.id.compareTo(r.fields.issueType.id) }
        @JvmField public val BY_PRIORITY: Comparator<Issue> = Comparator { l, r -> l.fields.priority.id.compareTo(r.fields.priority.id) }
        @JvmField public val BY_SUMMARY: Comparator<Issue> = Comparator { l, r -> l.fields.summary.compareTo(r.fields.summary) }
        @JvmField public val BY_ASSIGNEE: Comparator<Issue> = Comparator { l, r -> compareByAssignee(l, r) }
        @JvmField public val BY_MODIFIED: Comparator<Issue> = Comparator { l, r -> l.fields.updated.compareTo(r.fields.updated) }
        @JvmField public val BY_CREATED: Comparator<Issue> = Comparator { l, r -> l.fields.created.compareTo(r.fields.created) }

        private fun compareByAssignee(l: Issue, r: Issue): Int {
            val lAssignee = if (l.fields.assignee == null) "" else l.fields.assignee.name
            val rAssignee = if (r.fields.assignee == null) "" else r.fields.assignee.name
            return lAssignee.compareTo(rAssignee)
        }
    }


}
