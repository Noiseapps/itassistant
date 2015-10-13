package com.noiseapps.itassistant.fragment.issuedetails;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.adapters.WorkLogAdapter;
import com.noiseapps.itassistant.connector.JiraConnector;
import com.noiseapps.itassistant.model.jira.issues.Issue;
import com.noiseapps.itassistant.model.jira.issues.worklog.WorkLogItem;
import com.noiseapps.itassistant.model.jira.issues.worklog.WorkLogs;
import com.noiseapps.itassistant.utils.Consts;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;
import org.joda.time.MutableDateTime;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@EFragment(R.layout.fragment_work_log)
public class WorkLogFragment extends Fragment {

    @Bean
    JiraConnector jiraConnector;
    @FragmentArg
    Issue issue;
    @ViewById
    ListView workLogList;
    @ViewById
    FABProgressCircle fabProgressCircle;
    @ViewById
    FloatingActionButton addWorkLogFab;
    @ViewById
    View noWorkLogsView, loadingWorkLogs, errorView;
    private WorkLogAdapter adapter;

    @AfterViews
    void init() {
        adapter = new WorkLogAdapter(getContext(), new ArrayList<WorkLogItem>());
        workLogList.setAdapter(adapter);
        initViewVisibility();
        jiraConnector.getIssueWorkLog(issue.getId(), new WorkLogCallbacks());
    }

    private void initViewVisibility() {
        fabProgressCircle.setVisibility(View.GONE);
        noWorkLogsView.setVisibility(View.GONE);
        workLogList.setVisibility(View.GONE);
        loadingWorkLogs.setVisibility(View.VISIBLE);
    }

    private void showEmptyView() {
        noWorkLogsView.setVisibility(View.VISIBLE);
        workLogList.setVisibility(View.GONE);
        loadingWorkLogs.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
    }

    @Click(R.id.addWorkLogFab)
    void onAddWorkLog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.addWorkLog, issue.getKey()));
        builder.setView(R.layout.dialog_add_worklog);
        builder.setPositiveButton(R.string.post, null);
        builder.setNegativeButton(R.string.cancel, null);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                onDialogShown(alertDialog);
            }
        });
        alertDialog.show();
    }

    private void onDialogShown(final AlertDialog alertDialog) {
        final Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        final Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        final TextView editWorkLog = (TextView) alertDialog.findViewById(R.id.editWorkLogDate);
        final MutableDateTime dateTime = MutableDateTime.now();
        editWorkLog.setText(dateTime.toString(Consts.DATE_FORMAT));
        editWorkLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateTime.setYear(year);
                        dateTime.setMonthOfYear(monthOfYear + 1);
                        dateTime.setDayOfMonth(dayOfMonth);
                        dateTime.setMillisOfDay(0);
                        editWorkLog.setText(dateTime.toString(Consts.DATE_FORMAT));
                    }
                };
                final DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), onDateSetListener, dateTime.getYear(), dateTime.getMonthOfYear()-1, dateTime.getDayOfMonth());
                datePickerDialog.getDatePicker().setMaxDate(dateTime.getMillis());
                datePickerDialog.show();
            }
        });

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View dialogRoot = alertDialog.findViewById(R.id.dialogRoot);
                final EditText workedText = (EditText) alertDialog.findViewById(R.id.workedText);
                final EditText remainingText = (EditText) alertDialog.findViewById(R.id.remainingText);
                final EditText commentText = (EditText) alertDialog.findViewById(R.id.commentText);
                final String newEstimate = remainingText.getText().toString().trim();
                final String timeSpent = workedText.getText().toString().trim();
                boolean valid = validateInput(newEstimate, timeSpent, workedText, remainingText);
                if(valid) {
                    final WorkLogItem logItem = new WorkLogItem();
                    logItem.setComment(commentText.getText().toString());
                    logItem.setTimeSpent(timeSpent);
                    logItem.setStarted(dateTime.toString(Consts.TIMESTAMP_FORMAT));
                    onPositiveButtonClicked(logItem, newEstimate, alertDialog, dialogRoot);
                }
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private boolean validateInput(String newEstimate, String timeSpent, EditText workedText, EditText remainingText) {
        boolean valid = true;
        if(!validateWorkLog(timeSpent)) {
            valid = false;
            workedText.setError(getString(R.string.invalidFormat));
        }
        if(!validateWorkLog(newEstimate)) {
            valid = false;
            remainingText.setError(getString(R.string.invalidFormat));
        }
        return valid;
    }

    public boolean validateWorkLog(String workLog) {
        return !workLog.isEmpty() && Consts.PATTERN.matcher(workLog).matches();
    }

    private void onPositiveButtonClicked(WorkLogItem logItem, String newEstimate, final AlertDialog alertDialog, final View dialogRoot) {
        fabProgressCircle.show();
        addWorkLogFab.setEnabled(false);
        jiraConnector.postIssueWorkLog(issue.getId(), newEstimate, logItem, new Callback<WorkLogItem>() {
            @Override
            public void success(WorkLogItem logItem, Response response) {
                fabProgressCircle.beginFinalAnimation();
                Snackbar.make(fabProgressCircle, R.string.workLogAdded, Snackbar.LENGTH_LONG).show();
                addWorkLogFab.setEnabled(true);
                adapter.addItem(logItem);
                noWorkLogsView.setVisibility(View.GONE);
                workLogList.setVisibility(View.VISIBLE);
            }

            @Override
            public void failure(RetrofitError error) {
                addWorkLogFab.setEnabled(true);
                fabProgressCircle.hide();
                Snackbar.make(fabProgressCircle, R.string.failedToLogWork, Snackbar.LENGTH_LONG).show();
            }
        });
        alertDialog.dismiss();
    }

    private class WorkLogCallbacks implements Callback<WorkLogs> {
        @Override
        public void success(WorkLogs workLogs, Response response) {
            fabProgressCircle.setVisibility(View.VISIBLE);
            loadingWorkLogs.setVisibility(View.GONE);
            final List<WorkLogItem> worklogs = workLogs.getWorklogs();
            if(worklogs.isEmpty()) {
                showEmptyView();
            } else {
                adapter.setItems(worklogs);
                workLogList.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void failure(RetrofitError error) {
            loadingWorkLogs.setVisibility(View.GONE);
            errorView.setVisibility(View.VISIBLE);
        }
    }
}
