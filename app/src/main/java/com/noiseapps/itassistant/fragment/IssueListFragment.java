package com.noiseapps.itassistant.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.connector.JiraConnector;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.jira.issues.Assignee;
import com.noiseapps.itassistant.model.jira.issues.Issue;
import com.noiseapps.itassistant.model.jira.issues.JiraIssueList;
import com.noiseapps.itassistant.model.jira.issues.Project;
import com.noiseapps.itassistant.model.jira.issues.Status;
import com.noiseapps.itassistant.model.jira.projects.JiraProject;
import com.noiseapps.itassistant.utils.AuthenticatedPicasso;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    private ArrayList<Assignee> assignees;
    @NonNull
    private Callbacks mCallbacks = sDummyCallbacks;
    private boolean isEmpty;
    private Subscription projectDownloadSubscriber;
    private boolean[] checkedItems;
    private String[] filters;

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
        filters = getContext().getResources().getStringArray(R.array.issuesFilters);
        checkedItems = new boolean[filters.length];
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
        if(projectDownloadSubscriber != null) {
            projectDownloadSubscriber.unsubscribe();
        }
        setToolbarTitle(jiraProject.getName());
        this.jiraProject = jiraProject;
        this.baseAccount = baseAccount;
        displayData();
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
        final String projectKey = jiraProject.getKey();
        projectDownloadSubscriber = Observable.zip(jiraConnector.getProjectIssues(projectKey),
                jiraConnector.getProjectMembers(projectKey),
                (jiraIssueList, assignees) -> {
                    IssueListFragment.this.jiraIssueList = jiraIssueList;
                    IssueListFragment.this.assignees = new ArrayList<>(assignees);
                    return null;
                }).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(o -> onProjectsDownloaded(), throwable -> {
                    isEmpty = true;
                    noProject.setVisibility(View.GONE);
                    hideProgress(false);
                }, () -> projectDownloadSubscriber = null);
    }

    private void onProjectsDownloaded() {
        noProject.setVisibility(View.GONE);
        isEmpty = jiraIssueList.getIssues().isEmpty();
        final PagerAdapter adapter = makeAdapter(jiraIssueList.getIssues());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        hideProgress(false);
        setHasOptionsMenu(true);
    }

    @NonNull
    private PagerAdapter makeAdapter(List<Issue> jiraIssueList) {
        final Set<WorkflowObject> workflows = new HashSet<>();
        final ListMultimap<String, Issue> issuesInWorkflow = ArrayListMultimap.create();
        for (Issue issue : jiraIssueList) {
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

    @OptionsItem(R.id.actionFilter)
    public void showFilterDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.selectFilter);
        builder.setMultiChoiceItems(filters, checkedItems, (dialog, which, isChecked) -> {
            checkedItems[which] = isChecked;
        });
        builder.setPositiveButton(R.string.ok, (dialog, which) -> handleFilterSelected());
        builder.show();
    }

    private void handleFilterSelected() {
        if(!checkedItems[0] && !checkedItems[1]) {
            onListFiltered(jiraIssueList.getIssues());
            return;
        }
        final Predicate<Issue> assigneeFilter = new AssignedToMeFilter(checkedItems[0]);
        final Predicate<Issue> reporterFilter = new ReportedByMeFilter(checkedItems[1]);
        final Predicate<Issue> filterQuery = Predicates.or(assigneeFilter, reporterFilter);
        final Iterable<Issue> filteredIssues = Iterables.filter(jiraIssueList.getIssues(), filterQuery);
        final ArrayList<Issue> filteredList = Lists.newArrayList(filteredIssues);
        Logger.d("Before " + jiraIssueList.getIssues().size() + " After " + filteredList.size());
        onListFiltered(filteredList);
    }

    private void onListFiltered(List<Issue> filteredList) {
        final PagerAdapter pagerAdapter = makeAdapter(filteredList);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private class AssignedToMeFilter implements Predicate<Issue> {

        private final boolean active;

        private AssignedToMeFilter(boolean active) {
            this.active = active;
        }

        @Override
        public boolean apply(Issue input) {
            if(!active) {
                return false;
            }
            final Assignee assignee = input.getFields().getAssignee();
            final boolean result = assignee != null && baseAccount.getUsername().equalsIgnoreCase(assignee.getName());
            Logger.d(input.getKey() + ", " + result);
            return result;
        }
    }

    private class ReportedByMeFilter implements Predicate<Issue> {

        private final boolean active;

        private ReportedByMeFilter(boolean active) {
            this.active = active;
        }

        @Override
        public boolean apply(Issue input) {
            final String username = baseAccount.getUsername();
            final String reporter = input.getFields().getReporter().getName();
            final boolean result = active && username.equalsIgnoreCase(reporter);
            Logger.d(String.valueOf(result));
            return result;
        }
    }

    public void setIssues(List<Issue> myIssues) {
        if(projectDownloadSubscriber != null) {
            projectDownloadSubscriber.unsubscribe();
        }
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
        if (supportActionBar != null) {
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
                fragments[position] = JiraIssueListFragment_.builder().assignees(assignees).assignedToMe(assignedToMeScreen).
                        workflowName(pageTitle).build();
            }
            final ArrayList<Issue> issues = new ArrayList<>(issuesInWorkflow.get(pageTitle));
            ((JiraIssueListFragment) fragments[position]).setIssues(issues);
            return fragments[position];
        }
    }
}
