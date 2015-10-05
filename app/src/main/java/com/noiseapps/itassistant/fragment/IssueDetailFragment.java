package com.noiseapps.itassistant.fragment;

import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.model.jira.issues.Issue;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_issue_detail)
public class IssueDetailFragment extends Fragment {

    @ViewById
    TextView textView2;

    @FragmentArg
    Issue issue;

    @AfterViews
    void init() {
        setHasOptionsMenu(true);
        textView2.setText(issue.toString());
    }

    @OptionsItem(android.R.id.home)
    void onHome() {
        getActivity().finish();
    }
}
