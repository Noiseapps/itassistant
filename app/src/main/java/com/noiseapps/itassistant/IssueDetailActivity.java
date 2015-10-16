package com.noiseapps.itassistant;

import android.content.pm.ActivityInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.noiseapps.itassistant.fragment.IssueDetailFragment;
import com.noiseapps.itassistant.fragment.IssueDetailFragment_;
import com.noiseapps.itassistant.model.jira.issues.Issue;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_issue_detail)
public class IssueDetailActivity extends AppCompatActivity implements IssueDetailFragment.IssueDetailCallbacks {
    @Extra
    Issue issue;
    @ViewById(R.id.detail_toolbar)
    Toolbar toolbar;

    @Override
    public void onEditIssue(Issue issue) {
        final String key = issue.getFields().getProject().getKey();
        NewIssueActivity_.intent(this).projectKey(key).issue(issue).startForResult(1000);
    }

    @AfterViews
    void init() {
        setTablet();
        initToolbar();
        final IssueDetailFragment fragment = IssueDetailFragment_.builder().issue(issue).build();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.issue_detail_container, fragment)
                .commit();
    }


    private void setTablet() {
        if (getResources().getBoolean(R.bool.tabletSize)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        final ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setHomeButtonEnabled(true);
            supportActionBar.setTitle(String.format("%s (%s)", issue.getFields().getSummary(), issue.getKey()));
        }
    }
}
