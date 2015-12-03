package com.noiseapps.itassistant.fragment.issuedetails;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.noiseapps.itassistant.BuildConfig;
import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.database.PreferencesDAO;
import com.noiseapps.itassistant.fragment.IssueDetailFragment;
import com.noiseapps.itassistant.model.TimeTrackingInfo;
import com.noiseapps.itassistant.model.jira.issues.Assignee;
import com.noiseapps.itassistant.model.jira.issues.Fields;
import com.noiseapps.itassistant.model.jira.issues.Issue;
import com.noiseapps.itassistant.model.jira.issues.worklog.WorkLogItem;
import com.noiseapps.itassistant.utils.Consts;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

@EFragment(R.layout.fragment_general_info)
public class GeneralInfoFragment extends Fragment implements IssueDetailFragment.DetailFragmentCallbacks {
    private static final int WEEK_DAYS = 5;
    private static final int DAY_HOURS = 8;
    private static final int HOUR_MINUTES = 60;
    private static final int MINUTE_SECONDS = 60;
    private static final int SECOND_MILLIS = 1000;
    @ViewById
    TextView type, priority, labels, fixedInVersion, status, issueName, assignee, reporter, description, created, modified, version;
    @ViewById
    TextRoundCornerProgressBar estimated, left, logged;
    @FragmentArg
    Issue issue;
    @Bean
    PreferencesDAO preferencesDAO;

    @Override
    public void onFabClicked(FABProgressCircle circle) {
        TimeTrackingInfo timeTrackingInfo = preferencesDAO.getTimeTrackingInfo();
        if (timeTrackingInfo == null || timeTrackingInfo.getIssue() == null) {
            timeTrackingInfo = new TimeTrackingInfo(issue, DateTime.now().getMillis());
            preferencesDAO.setTimeTrackingInfo(timeTrackingInfo);
            ((IssueDetailFragment) getParentFragment()).setTimetrackingStarted();
        } else {
            onAddWorkLog();
        }
    }

    private void onAddWorkLog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.addWorkLog, issue.getKey()));
        builder.setView(R.layout.dialog_add_worklog);
        builder.setPositiveButton(R.string.save, null);
        builder.setNegativeButton(R.string.dontSave, null);
        builder.setNeutralButton(R.string.cancel, null);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(dialog -> onDialogShown(alertDialog));
        alertDialog.show();
    }

    private void onDialogShown(final AlertDialog alertDialog) {
        final TimeTrackingInfo timeTrackingInfo = preferencesDAO.getTimeTrackingInfo();
        final TextView editWorkLog = (TextView) alertDialog.findViewById(R.id.editWorkLogDate);
        final EditText workedText = (EditText) alertDialog.findViewById(R.id.workedText);
        final EditText remainingText = (EditText) alertDialog.findViewById(R.id.remainingText);
        final EditText commentText = (EditText) alertDialog.findViewById(R.id.commentText);
        final Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        final Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        final Button neutralButton = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL);

        initTimeTrackingInfo(timeTrackingInfo, workedText, remainingText, commentText);

        final MutableDateTime dateTime = MutableDateTime.now();
        editWorkLog.setText(dateTime.toString(Consts.DATE_FORMAT));
        editWorkLog.setOnClickListener(v -> showDatePicker(editWorkLog, dateTime));

        positiveButton.setOnClickListener(v -> {
            final String newEstimate = remainingText.getText().toString().trim();
            final String timeSpent = workedText.getText().toString().trim();
            boolean valid = validateInput(newEstimate, timeSpent, workedText, remainingText);
            if (valid) {
                final WorkLogItem logItem = new WorkLogItem();
                logItem.setComment(commentText.getText().toString());
                logItem.setTimeSpent(timeSpent);
                logItem.setStarted(dateTime.toString(Consts.TIMESTAMP_FORMAT));
                preferencesDAO.setTimeTrackingInfo(null);
                ((IssueDetailFragment) getParentFragment()).setTimetrackingStopped(logItem);
                alertDialog.dismiss();
            }
        });
        negativeButton.setOnClickListener(v1 -> {
            preferencesDAO.setTimeTrackingInfo(null);
            ((IssueDetailFragment) getParentFragment()).setTimetrackingStopped();
            alertDialog.dismiss();
        });
        neutralButton.setOnClickListener(v -> alertDialog.dismiss());
    }

    private void showDatePicker(TextView editWorkLog, MutableDateTime dateTime) {
        final DatePickerDialog.OnDateSetListener onDateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            dateTime.setYear(year);
            dateTime.setMonthOfYear(monthOfYear + 1);
            dateTime.setDayOfMonth(dayOfMonth);
            dateTime.setMillisOfDay(0);
            editWorkLog.setText(dateTime.toString(Consts.DATE_FORMAT));
        };
        final DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), onDateSetListener, dateTime.getYear(), dateTime.getMonthOfYear() - 1, dateTime.getDayOfMonth());
        datePickerDialog.getDatePicker().setMaxDate(dateTime.getMillis());
        datePickerDialog.show();
    }

    private void initTimeTrackingInfo(TimeTrackingInfo timeTrackingInfo, EditText workedText, EditText remainingText, EditText commentText) {
        final long timeTrackEnd = System.currentTimeMillis();
        long trackedMillis = timeTrackEnd - timeTrackingInfo.getStarted();
        if (BuildConfig.DEBUG) {
            trackedMillis = TimeUnit.HOURS.toMillis(4) + TimeUnit.MINUTES.toMillis(53);
        }
        final int millisWeek = WEEK_DAYS * DAY_HOURS * HOUR_MINUTES * MINUTE_SECONDS * SECOND_MILLIS;
        final int fullWeeks = (int) Math.floor(trackedMillis / millisWeek);
        trackedMillis -= millisWeek * fullWeeks;

        final int millisDay = DAY_HOURS * HOUR_MINUTES * MINUTE_SECONDS * SECOND_MILLIS;
        final int days = (int) Math.floor(trackedMillis / millisDay);
        trackedMillis -= millisDay * days;

        final int millisHour = HOUR_MINUTES * MINUTE_SECONDS * SECOND_MILLIS;
        final int fullHours = (int) Math.floor(trackedMillis / millisHour);
        trackedMillis -= millisHour * fullHours;

        final int millisMinutes = MINUTE_SECONDS * SECOND_MILLIS;
        final int fullMinutes = (int) Math.floor(trackedMillis / millisMinutes);

        final StrBuilder builder = new StrBuilder("");
        if (fullWeeks > 0) {
            builder.append(String.format("%dw ", fullWeeks));
        }
        if (days > 0) {
            builder.append(String.format("%dd ", days));
        }
        if (fullHours > 0) {
            builder.append(String.format("%dh ", fullHours));
        }
        if (fullMinutes > 0) {
            builder.append(String.format("%dm ", fullMinutes));
        }
        workedText.setText(builder.toString().trim());
        remainingText.setText(R.string.emptyTime);
        commentText.setText(getString(R.string.workingOnIssue, timeTrackingInfo.getIssue().getKey()));
    }

    private boolean validateInput(String newEstimate, String timeSpent, EditText workedText, EditText remainingText) {
        boolean valid = true;
        if (!validateWorkLog(timeSpent)) {
            valid = false;
            workedText.setError(getString(R.string.invalidFormat));
        }
        if (!validateWorkLog(newEstimate)) {
            valid = false;
            remainingText.setError(getString(R.string.invalidFormat));
        }
        return valid;
    }

    public boolean validateWorkLog(String workLog) {
        return !workLog.isEmpty() && Consts.PATTERN.matcher(workLog).matches();
    }

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
        issueName.setText(String.format("%2$s: %1$s", issueFields.getSummary(), issue.getKey()));
        issueName.setSelected(true);
        fixedInVersion.setText(StringUtils.join(issueFields.getFixVersions(), ", "));
        version.setText(StringUtils.join(issueFields.getVersions(), ", "));
        description.setText(issueFields.getDescription());
    }

    private void setPeopleData(Fields issueFields) {
        final Assignee assignee = issueFields.getAssignee();
        if (assignee != null) {
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
