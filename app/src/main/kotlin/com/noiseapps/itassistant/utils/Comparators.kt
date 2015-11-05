package com.noiseapps.itassistant.utils

import com.noiseapps.itassistant.model.jira.issues.Issue
import java.util.*

class Comparators {

    object ISSUE {
        val BY_KEY : Comparator<Issue> = Comparator { l, r -> l.key.compareTo(r.key) }
        val BY_TYPE : Comparator<Issue> = Comparator { l, r -> l.fields.issueType.id.compareTo(r.fields.issueType.id) }
        val BY_PRIORITY : Comparator<Issue> = Comparator { l, r -> l.fields.priority.id.compareTo(r.fields.priority.id) }
        val BY_SUMMARY : Comparator<Issue> = Comparator { l, r -> l.fields.summary.compareTo(r.fields.summary) }
        val BY_ASSIGNEE : Comparator<Issue> = Comparator { l, r -> compareByAssignee(l, r)}
        val BY_MODIFIED : Comparator<Issue> = Comparator { l, r -> l.fields.updated.compareTo(r.fields.updated) }
        val BY_CREATED : Comparator<Issue> = Comparator { l, r -> l.fields.created.compareTo(r.fields.created) }

        private fun compareByAssignee(l : Issue, r: Issue) : Int {
            val lAssignee = if(l.fields.assignee == null) "" else l.fields.assignee.name
            val rAssignee = if(r.fields.assignee == null) "" else r.fields.assignee.name
            return lAssignee.compareTo(rAssignee)
        }
    }
}
