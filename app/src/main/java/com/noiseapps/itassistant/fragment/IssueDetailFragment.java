package com.noiseapps.itassistant.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.connector.JiraConnector;
import com.noiseapps.itassistant.fragment.issuedetails.CommentsFragment_;
import com.noiseapps.itassistant.fragment.issuedetails.GeneralInfoFragment_;
import com.noiseapps.itassistant.fragment.issuedetails.WorkLogFragment_;
import com.noiseapps.itassistant.model.jira.issues.Issue;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_issue_detail)
@OptionsMenu(R.menu.menu_issue_details)
public class IssueDetailFragment extends Fragment {

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
    private IssueDetailCallbacks callbacks;

    public interface IssueDetailCallbacks {
        void onEditIssue(Issue issue);
    }

    @AfterViews
    void init() {
        callbacks = (IssueDetailCallbacks) getActivity();
        setHasOptionsMenu(true);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new PagerAdapter());
        viewPager.addOnPageChangeListener(new PageChangeListener());
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(selectedTab, false);
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
        }

        @Override
        public void onPageSelected(int position) {
            selectedTab = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
