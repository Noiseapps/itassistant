package com.noiseapps.itassistant.fragment.issuedetails;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.noiseapps.itassistant.AnalyticsTrackers;
import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.adapters.WorkLogAdapter;
import com.noiseapps.itassistant.connector.JiraConnector;
import com.noiseapps.itassistant.fragment.IssueDetailFragment;
import com.noiseapps.itassistant.model.jira.issues.Issue;
import com.noiseapps.itassistant.model.jira.issues.worklog.WorkLogItem;
import com.noiseapps.itassistant.utils.Consts;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;
import org.joda.time.MutableDateTime;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EFragment(R.layout.fragment_work_log)
public class WorkLogFragment extends Fragment implements IssueDetailFragment.DetailFragmentCallbacks {

    @Bean
    JiraConnector jiraConnector;
    @FragmentArg
    Issue issue;
    @ViewById
    RecyclerView workLogList;
    FABProgressCircle fabProgressCircle;
    @ViewById
    View noWorkLogsView, loadingWorkLogs, errorView;
    @Bean
    AnalyticsTrackers tracker;
    private WorkLogAdapter adapter;

    @AfterViews
    void init() {
        adapter = new WorkLogAdapter(getContext(), new ArrayList<>());
        workLogList.setLayoutManager(new LinearLayoutManager(getContext()));
        workLogList.setAdapter(adapter);
        initViewVisibility();
        getWorklogs();
    }

    @Background
    void getWorklogs() {
        jiraConnector.getIssueWorkLog(issue.getId()).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(this::onWorkLogsDownloaded, this::onDownloadError);
    }

    private void initViewVisibility() {
        noWorkLogsView.setVisibility(View.GONE);
        workLogList.setVisibility(View.GONE);
        loadingWorkLogs.setVisibility(View.VISIBLE);
    }

    void onAddWorkLog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.addWorkLog, issue.getKey()));
        builder.setView(R.layout.dialog_add_worklog);
        builder.setPositiveButton(R.string.post, null);
        builder.setNegativeButton(R.string.cancel, null);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(dialog -> onDialogShown(alertDialog));
        alertDialog.show();
    }

    private void onDialogShown(final AlertDialog alertDialog) {
        final EditText workedText = (EditText) alertDialog.findViewById(R.id.workedText);
        final EditText remainingText = (EditText) alertDialog.findViewById(R.id.remainingText);
        final EditText commentText = (EditText) alertDialog.findViewById(R.id.commentText);
        final Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        final Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        final TextView editWorkLog = (TextView) alertDialog.findViewById(R.id.editWorkLogDate);
        final MutableDateTime dateTime = MutableDateTime.now();
        editWorkLog.setText(dateTime.toString(Consts.DATE_FORMAT));
        editWorkLog.setOnClickListener(v -> {
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
        });

        positiveButton.setOnClickListener(v -> {
            final String newEstimate = remainingText.getText().toString().trim();
            final String timeSpent = workedText.getText().toString().trim();
            boolean valid = validateInput(newEstimate, timeSpent, workedText, remainingText);
            if (valid) {
                final WorkLogItem logItem = new WorkLogItem();
                logItem.setComment(commentText.getText().toString());
                logItem.setTimeSpent(timeSpent);
                logItem.setStarted(dateTime.toString(Consts.TIMESTAMP_FORMAT));
                onPositiveButtonClicked(logItem, newEstimate, alertDialog);
            }
        });
        negativeButton.setOnClickListener(v -> alertDialog.dismiss());

        remainingText.setText(R.string.emptyTime);
        commentText.setText(getString(R.string.workingOnIssue, issue.getKey()));
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

    private void onPositiveButtonClicked(WorkLogItem logItem, String newEstimate, final AlertDialog alertDialog) {
        fabProgressCircle.show();
        jiraConnector.postIssueWorkLog(issue.getId(), newEstimate, logItem, new Callback<WorkLogItem>() {
            @Override
            public void success(WorkLogItem logItem, Response response) {
                tracker.sendEvent(AnalyticsTrackers.SCREEN_ISSUE_DETAILS, AnalyticsTrackers.CATEGORY_ISSUES, "worklogAdded");
                fabProgressCircle.beginFinalAnimation();
                Snackbar.make(fabProgressCircle, R.string.workLogAdded, Snackbar.LENGTH_LONG).show();
                adapter.addItem(logItem);
                noWorkLogsView.setVisibility(View.GONE);
                workLogList.setVisibility(View.VISIBLE);
            }

            @Override
            public void failure(RetrofitError error) {
                tracker.sendEvent(AnalyticsTrackers.SCREEN_ISSUE_DETAILS, AnalyticsTrackers.CATEGORY_ISSUES, "worklogAddingFailed");
                fabProgressCircle.hide();
                Snackbar.make(fabProgressCircle, R.string.failedToLogWork, Snackbar.LENGTH_LONG).show();
            }
        });
        alertDialog.dismiss();
    }

    private void onDownloadError(Throwable throwable) {
        Logger.e(throwable, throwable.getMessage());
        loadingWorkLogs.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
    }

    private void onWorkLogsDownloaded(List<WorkLogItem> worklogs) {
//        fabProgressCircle.setVisibility(View.VISIBLE);
        loadingWorkLogs.setVisibility(View.GONE);
        if (worklogs.isEmpty()) {
            showEmptyView();
        } else {
            adapter.setItems(worklogs);
            workLogList.setVisibility(View.VISIBLE);
        }
    }

    private void showEmptyView() {
        noWorkLogsView.setVisibility(View.VISIBLE);
        workLogList.setVisibility(View.GONE);
        loadingWorkLogs.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
    }

    @Override
    public void onFabClicked(FABProgressCircle circle) {
        fabProgressCircle = circle;
        onAddWorkLog();
    }
}
