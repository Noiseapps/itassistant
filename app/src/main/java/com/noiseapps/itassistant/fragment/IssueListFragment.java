package com.noiseapps.itassistant.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.connector.JiraConnector;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.jira.issues.Issue;
import com.noiseapps.itassistant.model.jira.issues.JiraIssueList;
import com.noiseapps.itassistant.model.jira.issues.Project;
import com.noiseapps.itassistant.model.jira.issues.Status;
import com.noiseapps.itassistant.model.jira.projects.JiraProject;
import com.noiseapps.itassistant.utils.AuthenticatedPicasso;
import com.noiseapps.itassistant.utils.ToggleList;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@EFragment(R.layout.fragment_issue_list)
@OptionsMenu(R.menu.menu_issue_list)
public class IssueListFragment extends Fragment implements JiraIssueListFragment.IssueListCallback {

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(Issue id, JiraProject jiraProject) {
        }

        @Override
        public void onAddNewIssue(JiraProject jiraProject) {
        }

        @Override
        public void onEditIssue(Issue issue) {
        }
    };
    @Bean
    JiraConnector jiraConnector;
    @ViewById
    LinearLayout loadingView, tabView, emptyList, noProject;
    @ViewById
    ViewPager viewPager;
    @ViewById
    FABProgressCircle fabProgressCircle;
    @ViewById
    TabLayout tabLayout;
    @InstanceState
    JiraIssueList jiraIssueList;
    @InstanceState
    BaseAccount baseAccount;
    @InstanceState
    JiraProject jiraProject;
    @NonNull
    private Callbacks mCallbacks = sDummyCallbacks;
    private boolean isEmpty;

    @Override
    public void onItemSelected(Issue selectedIssue) {
        mCallbacks.onItemSelected(selectedIssue, jiraProject);
    }

    @Override
    public void onItemEdit(Issue issue) {
        mCallbacks.onEditIssue(issue);
    }

    @Override
    public void showFabProgress() {
        fabProgressCircle.show();
    }

    @Override
    public void hideFabProgress(boolean success) {
        reload();
    }

    @AfterViews
    void init() {
        setRetainInstance(true);
        setHasOptionsMenu(jiraIssueList != null);
        mCallbacks = (Callbacks) getActivity();
        final ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(jiraProject.getName());
        }
        if (jiraIssueList != null) {
            displayData();
        }
    }

    public void setProject(JiraProject jiraProject, BaseAccount baseAccount) {
        setToolbarTitle(jiraProject.getName());
        this.jiraProject = jiraProject;
        this.baseAccount = baseAccount;
        displayData();
        setHasOptionsMenu(true);
    }

    private void displayData() {
        showProgress();
        getIssues(baseAccount);
    }

    private void showProgress() {
        noProject.setVisibility(View.GONE);
        emptyList.setVisibility(View.GONE);
        tabView.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
    }

    @Background
    void getIssues(BaseAccount baseAccount) {
        jiraConnector.setCurrentConfig(baseAccount);
        AuthenticatedPicasso.setConfig(getActivity(), baseAccount);
        jiraConnector.getProjectIssues(jiraProject.getKey(), new Callback<JiraIssueList>() {
            @Override
            public void success(JiraIssueList jiraIssueList, Response response) {
                IssueListFragment.this.jiraIssueList = jiraIssueList;
                noProject.setVisibility(View.GONE);
                onProjectsDownloaded();
            }

            @Override
            public void failure(RetrofitError error) {
                isEmpty = true;
                noProject.setVisibility(View.GONE);
                hideProgress(false);
            }
        });
    }

    private void onProjectsDownloaded() {
        isEmpty = jiraIssueList.getIssues().isEmpty();
        final PagerAdapter adapter = makeAdapter(jiraIssueList);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        hideProgress(false);
    }

    @NonNull
    private PagerAdapter makeAdapter(JiraIssueList jiraIssueList) {
        final Set<WorkflowObject> workflows = new HashSet<>();
        final ListMultimap<String, Issue> issuesInWorkflow = ArrayListMultimap.create();
        for (Issue issue : jiraIssueList.getIssues()) {
            final Status status = issue.getFields().getStatus();
            final String id = status.getId();
            final String name = status.getName();
            workflows.add(new WorkflowObject(id, name));
            issuesInWorkflow.put(name, issue);
        }
        return new WorkflowAdapter(workflows, issuesInWorkflow, false);
    }

    @UiThread
    void hideProgress(boolean hideActionButton) {
        loadingView.setVisibility(View.GONE);
        fabProgressCircle.setVisibility(hideActionButton ? View.GONE : View.VISIBLE);
        if (isEmpty) {
            emptyList.setVisibility(View.VISIBLE);
            tabView.setVisibility(View.GONE);
        } else {
            tabView.setVisibility(View.VISIBLE);
            emptyList.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = sDummyCallbacks;
    }

    @Click(R.id.addIssueFab)
    void onAddNewIssue() {
        mCallbacks.onAddNewIssue(jiraProject);
    }

    @OptionsItem(R.id.actionRefresh)
    public void reload() {
        jiraIssueList = null;
        setProject(jiraProject, baseAccount);
    }

    public void setIssues(List<Issue> myIssues) {
        showProgress();
        isEmpty = myIssues.isEmpty();
        setToolbarTitle(getString(R.string.myIssues));
        final PagerAdapter pagerAdapter = makeMyIssuesAdapter(myIssues);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        hideProgress(true);
    }

    private void setToolbarTitle(String title) {
        final ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if(supportActionBar != null) {
            supportActionBar.setTitle(title);
        }
    }

    private PagerAdapter makeMyIssuesAdapter(List<Issue> myIssues) {
        final Set<WorkflowObject> workflows = new HashSet<>();
        final ListMultimap<String, Issue> issuesInWorkflow = ArrayListMultimap.create();
        for (Issue issue : myIssues) {
            final Project project = issue.getFields().getProject();
            final String id = project.getId();
            final String name = project.getKey();
            workflows.add(new WorkflowObject(id, name));
            issuesInWorkflow.put(name, issue);
        }
        return new WorkflowAdapter(workflows, issuesInWorkflow, true);
    }

    public interface Callbacks {
        void onItemSelected(Issue id, JiraProject jiraProject);

        void onAddNewIssue(JiraProject jiraProject);

        void onEditIssue(Issue issue);
    }

    private class WorkflowObject {
        final int order;
        final String name;

        private WorkflowObject(String order, String name) {
            this.order = Integer.parseInt(order);
            this.name = name;
        }

        @SuppressWarnings("SimplifiableIfStatement")
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            WorkflowObject that = (WorkflowObject) o;

            if (order != that.order) return false;
            return !(name != null ? !name.equals(that.name) : that.name != null);

        }

        @Override
        public int hashCode() {
            int result = order;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "WorkflowObject{" +
                    "order=" + order +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    private class WorkflowAdapter extends FragmentStatePagerAdapter {
        private final Fragment[] fragments;
        private final ListMultimap<String, Issue> issuesInWorkflow;
        private final boolean assignedToMeScreen;
        private List<WorkflowObject> workflows;

        public WorkflowAdapter(Set<WorkflowObject> workflows, ListMultimap<String, Issue> issuesInWorkflow, boolean assignedToMeScreen) {
            super(getChildFragmentManager());
            this.issuesInWorkflow = issuesInWorkflow;
            this.assignedToMeScreen = assignedToMeScreen;
            this.workflows = new ArrayList<>(workflows);
            Collections.sort(this.workflows, (lhs, rhs) -> lhs.order - rhs.order);
            fragments = new Fragment[workflows.size()];
        }

        @Override
        public int getCount() {
            return workflows.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return workflows.get(position).name;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            final String pageTitle = String.valueOf(getPageTitle(position));
            if (fragments[position] == null) {
                fragments[position] = JiraIssueListFragment_.builder().assignedToMe(assignedToMeScreen).
                        workflowName(pageTitle).build();
            }
            final ArrayList<Issue> issues = new ArrayList<>(issuesInWorkflow.get(pageTitle));
            ((JiraIssueListFragment) fragments[position]).setIssues(issues);
            return fragments[position];
        }
    }
}
