package com.noiseapps.itassistant.fragment;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.connector.JiraConnector;
import com.noiseapps.itassistant.database.PreferencesDAO;
import com.noiseapps.itassistant.fragment.issuedetails.CommentsFragment_;
import com.noiseapps.itassistant.fragment.issuedetails.GeneralInfoFragment_;
import com.noiseapps.itassistant.fragment.issuedetails.WorkLogFragment_;
import com.noiseapps.itassistant.model.TimeTrackingInfo;
import com.noiseapps.itassistant.model.jira.issues.Issue;
import com.noiseapps.itassistant.model.jira.issues.worklog.WorkLogItem;
import com.noiseapps.itassistant.utils.Consts;
import com.noiseapps.itassistant.utils.FragmentCallbacks;
import com.noiseapps.itassistant.utils.views.MyFabProgressCircle;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.joda.time.DateTime;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@EFragment(R.layout.fragment_issue_detail)
@OptionsMenu(R.menu.menu_issue_details)
public class IssueDetailFragment extends Fragment implements FragmentCallbacks {

    @InstanceState
    int selectedTab;
    @FragmentArg
    Issue issue;
    @Bean
    JiraConnector jiraConnector;
    @ViewById(R.id.detailViewPager)
    ViewPager viewPager;
    @ViewById(R.id.detailTabLayout)
    TabLayout tabLayout;
    @ViewById(R.id.detail_toolbar)
    Toolbar toolbar;
    @ViewById
    MyFabProgressCircle fabProgressCircle;
    @ViewById
    FloatingActionButton addWorkLogFab;
    @Bean
    PreferencesDAO preferencesDAO;
    private IssueDetailCallbacks callbacks;
    private DetailFragmentCallbacks childFragmentReceiver;
    private PagerAdapter pagerAdapter;

    public void setTimetrackingStarted() {
        setFabIcon(0);
        Snackbar.make(fabProgressCircle, getString(R.string.progressStarted, issue.getKey()), Snackbar.LENGTH_LONG).show();
    }
    public void setTimetrackingStopped() {
        setFabIcon(0);
        Snackbar.make(fabProgressCircle, getString(R.string.progressCleared, issue.getKey()), Snackbar.LENGTH_LONG).show();
    }

    public void setTimetrackingStopped(WorkLogItem logItem) {
        fabProgressCircle.show();
        jiraConnector.postIssueWorkLog(issue.getId(), "0", logItem, new Callback<WorkLogItem>() {
            @Override
            public void success(WorkLogItem logItem, Response response) {
                fabProgressCircle.beginFinalAnimation();
                Snackbar.make(fabProgressCircle, R.string.workLogAdded, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void failure(RetrofitError error) {
                fabProgressCircle.hide();
                Snackbar.make(fabProgressCircle, R.string.failedToLogWork, Snackbar.LENGTH_LONG).show();
            }
        });
    }


    public interface DetailFragmentCallbacks {
        void onFabClicked(FABProgressCircle circle);
    }

    @Click(R.id.addWorkLogFab)
    void performFabAction() {
        childFragmentReceiver.onFabClicked(fabProgressCircle);
    }

    private void setFabIcon(int page) {
        if(fabProgressCircle.isCollapsed()){
            fabProgressCircle.expand();
        }
        switch (page) {
            case 0:
                if(preferencesDAO.getTimeTrackingInfo() == null ||
                        preferencesDAO.getTimeTrackingInfo().getIssue() == null){
                    addWorkLogFab.setImageResource(R.drawable.ic_timer_white_24dp);
                } else {
                    addWorkLogFab.setImageResource(R.drawable.ic_timer_off_white_24dp);
                }
                break;
            case 1:
                addWorkLogFab.setImageResource(R.drawable.ic_comment_white_24px);
                break;
            case 2:
                addWorkLogFab.setImageResource(R.drawable.ic_save_white_24px);
                break;
        }
    }

    public interface IssueDetailCallbacks {
        void onEditIssue(Issue issue);
    }

    @AfterViews
    void init() {
        callbacks = (IssueDetailCallbacks) getActivity();
        setHasOptionsMenu(true);
        viewPager.setOffscreenPageLimit(3);
        pagerAdapter = new PagerAdapter();
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new PageChangeListener());
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(selectedTab, false);
        initToolbar();
    }

    private void initToolbar() {
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(activity, R.color.white));
        final ActionBar supportActionBar = activity.getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setHomeButtonEnabled(true);
            supportActionBar.setTitle(String.format("%s (%s)", issue.getFields().getSummary(), issue.getKey()));
        }
    }

    @OptionsItem(R.id.action_edit)
    void onEditIssue() {
        callbacks.onEditIssue(issue);
    }

    @OptionsItem(android.R.id.home)
    void onHome() {
        getActivity().finish();
    }

    final class PagerAdapter extends FragmentStatePagerAdapter {

        private final Fragment[] fragments = new Fragment[3];
        private final String[] titles;

        public PagerAdapter() {
            super(getChildFragmentManager());
            initFragments();
            titles = getActivity().getResources().getStringArray(R.array.accountDetailsSections);
        }

        private void initFragments() {
            fragments[0] = GeneralInfoFragment_.builder().issue(issue).build();
            fragments[1] = CommentsFragment_.builder().issue(issue).build();
            fragments[2] = WorkLogFragment_.builder().issue(issue).build();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            selectedTab = position;
            setFabIcon(position);
            childFragmentReceiver = (DetailFragmentCallbacks) pagerAdapter.getItem(position);
        }

        @Override
        public void onPageSelected(int position) {
            selectedTab = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if(state == ViewPager.SCROLL_STATE_DRAGGING) {
                addWorkLogFab.setEnabled(false);
            } else if(state == ViewPager.SCROLL_STATE_IDLE) {
                addWorkLogFab.setEnabled(true);
            }
        }
    }
}
