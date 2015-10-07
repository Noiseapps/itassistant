package com.noiseapps.itassistant.fragment.issuedetails;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.adapters.WorkLogAdapter;
import com.noiseapps.itassistant.connector.JiraConnector;
import com.noiseapps.itassistant.model.jira.issues.Issue;
import com.noiseapps.itassistant.model.jira.issues.worklog.WorkLogItem;
import com.noiseapps.itassistant.model.jira.issues.worklog.WorkLogs;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

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
    FloatingActionButton addCommentFab;
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
