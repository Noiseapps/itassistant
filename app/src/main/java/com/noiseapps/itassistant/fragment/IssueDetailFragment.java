package com.noiseapps.itassistant.fragment;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.connector.JiraConnector;
import com.noiseapps.itassistant.fragment.issuedetails.CommentsFragment_;
import com.noiseapps.itassistant.fragment.issuedetails.GeneralInfoFragment_;
import com.noiseapps.itassistant.model.jira.issues.Issue;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_issue_detail)
@OptionsMenu(R.menu.menu_issue_details)
public class IssueDetailFragment extends Fragment {

    @ViewById
    View scrollView, loadingComments, comments;

    @FragmentArg
    Issue issue;
    @Bean
    JiraConnector jiraConnector;
    @ViewById(R.id.detailViewPager)
    ViewPager viewPager;
    @ViewById(R.id.detailTabLayout)
    TabLayout tabLayout;

    @AfterViews
    void init() {
        setHasOptionsMenu(true);
        viewPager.setAdapter(new PagerAdapter());
        tabLayout.setupWithViewPager(viewPager);
    }

    @OptionsItem(R.id.action_edit)
    void onEditIssue() {
        Snackbar.make(scrollView, R.string.optionUnavailable, Snackbar.LENGTH_LONG).show();
    }

    @OptionsItem(android.R.id.home)
    void onHome() {
        getActivity().finish();
    }


    final class PagerAdapter extends FragmentStatePagerAdapter {

        private final Fragment[] fragments = new Fragment[2];
        public PagerAdapter() {
            super(getChildFragmentManager());
            initFragments();
        }

        private void initFragments() {
            fragments[0] = GeneralInfoFragment_.builder().issue(issue).build();
            fragments[1] = CommentsFragment_.builder().issue(issue).build();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // TODO change
            return "";
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
}
