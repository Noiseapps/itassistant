package com.noiseapps.itassistant.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.connector.JiraConnector;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.jira.issues.Assignee;
import com.noiseapps.itassistant.model.jira.issues.Fields;
import com.noiseapps.itassistant.model.jira.issues.FixVersion;
import com.noiseapps.itassistant.model.jira.issues.Issue;
import com.noiseapps.itassistant.model.jira.issues.Project;
import com.noiseapps.itassistant.model.jira.issues.Status;
import com.noiseapps.itassistant.model.jira.projects.JiraProject;
import com.noiseapps.itassistant.utils.AuthenticatedPicasso;
import com.noiseapps.itassistant.utils.Comparators;
import com.noiseapps.itassistant.utils.views.MyFabProgressCircle;
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
import java.util.Comparator;
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
        public void onItemSelected(Issue id) {
            Logger.d("DummyItemSelected");
        }

        @Override
        public void onAddNewIssue(JiraProject jiraProject) {
            Logger.d("DummyItemSelected");
        }

        @Override
        public void onEditIssue(Issue issue) {
            Logger.d("DummyItemSelected");
        }
    };
    @Bean
    JiraConnector jiraConnector;
    @ViewById
    CoordinatorLayout coordinatorLayout;
    @ViewById
    LinearLayout loadingView, tabView, emptyList, noProject;
    @ViewById
    ViewPager viewPager;
    @ViewById
    MyFabProgressCircle fabProgressCircle;
    @ViewById
    TabLayout tabLayout;
    @InstanceState
    ArrayList<Issue> issues;
    //    JiraIssueList jiraIssueList;
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
    private String[] filters, sorts, splits;
    private int selectedSort;
    private SearchView actionView;

    @Override
    public void onItemSelected(Issue selectedIssue) {
        mCallbacks.onItemSelected(selectedIssue);
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
        sorts = getContext().getResources().getStringArray(R.array.issuesSort);
        splits = getContext().getResources().getStringArray(R.array.issuesSplits);
        checkedItems = new boolean[filters.length];
        setRetainInstance(true);
        setHasOptionsMenu(issues != null);
        mCallbacks = (Callbacks) getActivity();
        final ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null && jiraProject != null) {
            supportActionBar.setTitle(jiraProject.getName());
        }
        if (issues != null) {
            displayData();
        }
    }

    public void setProject(JiraProject jiraProject, BaseAccount baseAccount) {
        setHasOptionsMenu(false);
        if (projectDownloadSubscriber != null) {
            projectDownloadSubscriber.unsubscribe();
        }
        setToolbarTitle(jiraProject.getName());
        this.jiraProject = jiraProject;
        this.baseAccount = baseAccount;
        getActivity().invalidateOptionsMenu();
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
                    IssueListFragment.this.issues = new ArrayList<>(jiraIssueList.getIssues());
                    IssueListFragment.this.assignees = new ArrayList<>(assignees);
                    return null;
                }).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(o -> onProjectsDownloaded(false), throwable -> {
                    isEmpty = true;
                    noProject.setVisibility(View.GONE);
                    hideProgress(false, false);
                    setHasOptionsMenu(true);
                }, () -> {
                    projectDownloadSubscriber = null;
                    setHasOptionsMenu(true);
                });
    }

    private void onProjectsDownloaded(boolean assignedToMe) {
        if (assignedToMe) {
            fabProgressCircle.setVisibility(View.GONE);
        }
        noProject.setVisibility(View.GONE);
        isEmpty = issues.isEmpty();
        final PagerAdapter adapter = fillAdapter(issues, assignedToMe);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        hideProgress(false, assignedToMe);
        setHasOptionsMenu(true);
        handleFilterSelected();
        setHasOptionsMenu(true);
    }

    private PagerAdapter fillAdapter(List<Issue> jiraIssueList, boolean assignedToMe) {
        final Set<WorkflowObject> workflows = new HashSet<>();
        final ListMultimap<String, Issue> issuesInWorkflow = ArrayListMultimap.create();
        for (Issue issue : jiraIssueList) {
            final String name, id;
            if (assignedToMe) {
                final Project project = issue.getFields().getProject();
                id = project.getId();
                name = project.getKey();
            } else {
                final Status status = issue.getFields().getStatus();
                id = status.getId();
                name = status.getName();
            }
            workflows.add(new WorkflowObject(id, name));
            issuesInWorkflow.put(name, issue);
        }
        return new WorkflowAdapter(workflows, issuesInWorkflow, false);
    }

    @UiThread
    void hideProgress(boolean hideActionButton, boolean assignedToMe) {
        loadingView.setVisibility(View.GONE);
        setFabVisibility(hideActionButton, assignedToMe);
        if (isEmpty) {
            emptyList.setVisibility(View.VISIBLE);
            tabView.setVisibility(View.GONE);
        } else {
            tabView.setVisibility(View.VISIBLE);
            emptyList.setVisibility(View.GONE);
        }
    }

    private void setFabVisibility(boolean hideActionButton, boolean assignedToMe) {
        if (assignedToMe || hideActionButton) {
            fabProgressCircle.collapse();
            fabProgressCircle.setVisibility(View.GONE);
        } else {
            fabProgressCircle.expand();
            fabProgressCircle.setVisibility(View.VISIBLE);
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
        issues = null;
        setProject(jiraProject, baseAccount);
    }

    @OptionsItem(R.id.actionSort)
    public void sort() {
        final AlertDialogWrapper.Builder builder = new AlertDialogWrapper.Builder(getActivity());
        builder.setTitle(R.string.selectSort);
        builder.setSingleChoiceItems(sorts, selectedSort, (dialog, which) -> selectedSort = which);
        builder.setPositiveButton(R.string.sort, (dialog, which) -> {
            handleSortIssues(selectedSort);
        });

        builder.show();
    }

    @OptionsItem(R.id.actionSplitBy)
    public void onSplitSelected() {
        final AlertDialogWrapper.Builder builder = new AlertDialogWrapper.Builder(getActivity());
        builder.setTitle(R.string.selectSplit);
        builder.setSingleChoiceItems(splits, selectedSort, (dialog, which) -> selectedSort = which);
        builder.setPositiveButton(R.string.sort, (dialog, which) -> {
            final List<Issue> issues = this.issues;
            final PagerAdapter pagerAdapter = fillAdapter(issues, selectedSort == 0);
            viewPager.setAdapter(pagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
        });

        builder.show();
    }

    private void handleSortIssues(int which) {
        selectedSort = which;
        Comparator<Issue> comparator = (lhs, rhs) -> 0;
        switch (which) {
            case 0:
                break;
            case 1:
                comparator = Comparators.ISSUE.BY_KEY;
                break;
            case 2:
                comparator = Comparators.ISSUE.BY_TYPE;
                break;
            case 3:
                comparator = Comparators.ISSUE.BY_PRIORITY;
                break;
            case 4:
                comparator = Comparators.ISSUE.BY_SUMMARY;
                break;
            case 5:
                comparator = Comparators.ISSUE.BY_ASSIGNEE;
                break;
            case 6:
                comparator = Comparators.ISSUE.BY_MODIFIED;
                break;
            case 7:
                comparator = Comparators.ISSUE.BY_CREATED;
                break;
            default:
                break;
        }
        final List<Issue> issues = new ArrayList<>(this.issues);
        Collections.sort(issues, comparator);
        onListFiltered(issues);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        final MenuItem item = menu.findItem(R.id.actionSearch);
        actionView = (SearchView) MenuItemCompat.getActionView(item);
        actionView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                final List<Issue> issues = IssueListFragment.this.issues;
                if (query.isEmpty()) {
                    onListFiltered(issues);
                    return true;
                }
                final Iterable<Issue> filter = Iterables.filter(issues, new SearchFilter(query));
                final ArrayList<Issue> filtered = Lists.newArrayList(filter);
                onListFiltered(filtered);
                Logger.d(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                final List<Issue> issues = IssueListFragment.this.issues;
                if (query.isEmpty()) {
                    onListFiltered(issues);
                    return true;
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
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

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.actionRefresh).setVisible(baseAccount != null);
        menu.findItem(R.id.actionFilter).setVisible(baseAccount != null);
        menu.findItem(R.id.actionSplitBy).setVisible(baseAccount == null);
    }

    public boolean isSearchViewOpen() {
        return !actionView.isIconified();
    }

    private void handleFilterSelected() {
        if (!checkedItems[0] && !checkedItems[1] && !checkedItems[2]) {
            onListFiltered(issues);
            return;
        }
        final Predicate<Issue> assigneeFilter = new AssignedToMeFilter(checkedItems[0]);
        final Predicate<Issue> reporterFilter = new ReportedByMeFilter(checkedItems[1]);
        final Predicate<Issue> unreleasedFilter = new UnreleasedFilter(checkedItems[2]);
        //noinspection unchecked
        final Predicate<Issue> filterQuery = Predicates.and(assigneeFilter, reporterFilter, unreleasedFilter);
        final Iterable<Issue> filteredIssues = Iterables.filter(issues, filterQuery);
        final ArrayList<Issue> filteredList = Lists.newArrayList(filteredIssues);
        onListFiltered(filteredList);
    }

    private void onListFiltered(List<Issue> filteredList) {
        final PagerAdapter pagerAdapter = fillAdapter(filteredList, jiraProject == null && selectedSort == 0);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void setIssues(List<Issue> myIssues) {
        if (projectDownloadSubscriber != null) {
            projectDownloadSubscriber.unsubscribe();
        }
        jiraProject = null;
        baseAccount = null;
        this.issues = new ArrayList<>();
        if(myIssues != null) {
            issues.addAll(myIssues);
        }
        showProgress();
        setToolbarTitle(getString(R.string.myIssues));
        onProjectsDownloaded(true);
        getActivity().invalidateOptionsMenu();
    }

    private void setToolbarTitle(String title) {
        final ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(title);
        }
    }

    public void closeSearchView() {
        actionView.clearFocus();
        actionView.setIconified(true);
    }

    public interface Callbacks {
        void onItemSelected(Issue id);

        void onAddNewIssue(JiraProject jiraProject);

        void onEditIssue(Issue issue);
    }

    private class AssignedToMeFilter implements Predicate<Issue> {

        private final boolean active;

        private AssignedToMeFilter(boolean active) {
            this.active = active;
        }

        @Override
        public boolean apply(Issue input) {
            if (!active) {
                return true;
            }
            final Assignee assignee = input.getFields().getAssignee();
            return assignee != null && baseAccount.getUsername().equalsIgnoreCase(assignee.getName());
        }
    }

    private class UnreleasedFilter implements Predicate<Issue> {

        private final boolean active;

        private UnreleasedFilter(boolean active) {
            this.active = active;
        }

        @Override
        public boolean apply(Issue input) {
            if (!active) {
                return true;
            }
            final List<FixVersion> fixVersions = input.getFields().getFixVersions();
            return fixVersions.isEmpty();
        }
    }

    private class ReportedByMeFilter implements Predicate<Issue> {

        private final boolean active;

        private ReportedByMeFilter(boolean active) {
            this.active = active;
        }

        @Override
        public boolean apply(Issue input) {
            if (!active) {
                return true;
            }
            final String username = baseAccount.getUsername();
            final String reporter = input.getFields().getReporter().getName();
            final boolean result = username.equalsIgnoreCase(reporter);
            Logger.d(String.valueOf(result));
            return result;
        }
    }

    private class SearchFilter implements Predicate<Issue> {
        final String query;

        private SearchFilter(String query) {
            this.query = query;
        }

        @Override
        public boolean apply(Issue issue) {
            final String lowerQuery = query.toLowerCase();
            final boolean keyContains = issue.getKey().toLowerCase().contains(lowerQuery);
            final Fields fields = issue.getFields();
            final boolean summaryContains = fields.getSummary().toLowerCase().contains(lowerQuery);
            final boolean descriptionContains = fields.getDescription() != null && fields.getDescription().toLowerCase().contains(lowerQuery);
            final boolean assigneeContains = fields.getAssignee() != null && fields.getAssignee().getDisplayName().toLowerCase().contains(lowerQuery);
            return keyContains || summaryContains || descriptionContains || assigneeContains;
        }
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
