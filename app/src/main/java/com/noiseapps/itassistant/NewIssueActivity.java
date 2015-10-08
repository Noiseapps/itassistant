package com.noiseapps.itassistant;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.noiseapps.itassistant.fragment.NewIssueFragment;
import com.noiseapps.itassistant.fragment.NewIssueFragment_;
import com.noiseapps.itassistant.model.jira.issues.Issue;
import com.noiseapps.itassistant.model.jira.projects.JiraProject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_new_issue)
public class NewIssueActivity extends AppCompatActivity implements NewIssueFragment.NewIssueCallbacks {

    @ViewById
    Toolbar toolbar;
    @Extra
    Issue issue;
    @Extra
    JiraProject project;

    @AfterViews
    void init() {
        setResult(RESULT_CANCELED);
        initToolbar();
        final NewIssueFragment fragment = NewIssueFragment_.builder().project(project).issue(issue).build();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        final ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setHomeButtonEnabled(true);
            if (issue == null) {
                supportActionBar.setTitle(getString(R.string.newIssueForProject, project.getName()));
            } else {
                supportActionBar.setTitle(getString(R.string.editingIssue, issue.getKey()));
            }
        }
    }

    @OptionsItem(android.R.id.home)
    void onHome() {
        finish();
    }

    @Override
    public void onIssueCreated() {
        setResult(RESULT_OK);
        finish();
    }
}
