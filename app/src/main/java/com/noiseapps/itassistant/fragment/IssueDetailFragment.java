package com.noiseapps.itassistant.fragment;

import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.model.jira.issues.Assignee;
import com.noiseapps.itassistant.model.jira.issues.Fields;
import com.noiseapps.itassistant.model.jira.issues.Issue;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;
import org.apache.commons.lang3.StringUtils;

@EFragment(R.layout.fragment_issue_detail)
public class IssueDetailFragment extends Fragment {

    @ViewById
    TextView type, priority, labels, status, issueName, assignee, reporter, description;

    @FragmentArg
    Issue issue;

    @AfterViews
    void init() {
        setHasOptionsMenu(true);
        fillTextViews();
    }

    private void fillTextViews() {
        final Fields issueFields = issue.getFields();
        type.setText(issueFields.getIssuetype().getName());
        priority.setText(issueFields.getPriority().getName());
        status.setText(issueFields.getStatus().getName());
        labels.setText(StringUtils.join(issueFields.getLabels(), ", "));
        final Assignee assignee = issueFields.getAssignee();
        if(assignee != null) {
            this.assignee.setText(assignee.getDisplayName());
        }
        reporter.setText(issueFields.getReporter().getDisplayName());
        issueName.setText(String.format("%s (%s)", issueFields.getSummary(), issue.getKey()));
        description.setText(issueFields.getDescription());
    }

    @OptionsItem(android.R.id.home)
    void onHome() {
        getActivity().finish();
    }
}
