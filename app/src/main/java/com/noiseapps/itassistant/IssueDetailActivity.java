package com.noiseapps.itassistant;

import android.support.v7.app.AppCompatActivity;

import com.noiseapps.itassistant.fragment.IssueDetailFragment;
import com.noiseapps.itassistant.fragment.IssueDetailFragment_;
import com.noiseapps.itassistant.model.jira.issues.Issue;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

@EActivity(R.layout.activity_issue_detail)
public class IssueDetailActivity extends AppCompatActivity {
    @Extra
    Issue issue;

    @AfterViews
    void init() {
        final IssueDetailFragment fragment = IssueDetailFragment_.builder().issue(issue).build();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.issue_detail_container, fragment)
                .commit();
    }
}
