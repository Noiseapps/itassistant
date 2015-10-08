package com.noiseapps.itassistant.fragment.issuedetails;

import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar;
import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.model.jira.issues.Assignee;
import com.noiseapps.itassistant.model.jira.issues.Fields;
import com.noiseapps.itassistant.model.jira.issues.Issue;
import com.noiseapps.itassistant.utils.Consts;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.joda.time.DateTime;

@EFragment(R.layout.fragment_general_info)
public class GeneralInfoFragment extends Fragment {
    @ViewById
    TextView type, priority, labels, status, issueName, assignee, reporter, description, created, modified;
    @ViewById
    TextRoundCornerProgressBar estimated, left, logged;
    @FragmentArg
    Issue issue;

    @AfterViews
    void init() {

        final Fields issueFields = issue.getFields();
        setIssueDetailsData(issueFields);
        setProgressData(issueFields);
        setDatesData(issueFields);
        setPeopleData(issueFields);
    }


    private void setIssueDetailsData(Fields issueFields) {
        type.setText(issueFields.getIssueType().getName());
        priority.setText(issueFields.getPriority().getName());
        status.setText(issueFields.getStatus().getName());
        labels.setText(StringUtils.join(issueFields.getLabels(), ", "));
        issueName.setText(String.format("%s (%s)", issueFields.getSummary(), issue.getKey()));
        description.setText(issueFields.getDescription());
    }

    private void setPeopleData(Fields issueFields) {
        final Assignee assignee = issueFields.getAssignee();
        if(assignee != null) {
            this.assignee.setText(assignee.getDisplayName());
        } else {
            this.assignee.setText(R.string.notAssigned);
        }
        reporter.setText(issueFields.getReporter().getDisplayName());
    }

    private void setDatesData(Fields issueFields) {
        final String createdTime = DateTime.parse(issueFields.getCreated()).toString(Consts.DATE_TIME_FORMAT);
        created.setText(createdTime);
        final String updatedTime = DateTime.parse(issueFields.getUpdated()).toString(Consts.DATE_TIME_FORMAT);
        modified.setText(updatedTime);
    }

    private void setProgressData(Fields issueFields) {
        final long originalEstimate = issueFields.getAggregatetimeoriginalestimate();
        final long timeSpent = issueFields.getTimespent();
        long max = Math.max(originalEstimate, timeSpent);
        final String estimateDuration = DurationFormatUtils.formatDuration(originalEstimate * 1000, getContext().getString(R.string.timeFormat));
        final String spentDuration = DurationFormatUtils.formatDuration(timeSpent * 1000, getContext().getString(R.string.timeFormat));

        estimated.setMax(max);
        estimated.setProgress(originalEstimate);
        estimated.setProgressText(estimateDuration);

        left.setMax(max);

        logged.setMax(max);
        logged.setProgress(timeSpent);
        logged.setProgressText(spentDuration);
    }
}
