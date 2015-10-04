package com.noiseapps.itassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.noiseapps.itassistant.fragment.IssueDetailFragment;
import com.noiseapps.itassistant.fragment.IssueListFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_issue_app_bar)
public class IssueListActivity extends AppCompatActivity
        implements IssueListFragment.Callbacks {

    private boolean mTwoPane;

    @ViewById
    Toolbar toolbar;

    @AfterViews
    void init() {
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        isTwoPane();
    }

    private void isTwoPane() {
        if (findViewById(R.id.issue_detail_container) != null) {
            mTwoPane = true;
        }
    }

    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            final Bundle arguments = new Bundle();
            arguments.putString(IssueDetailFragment.ARG_ITEM_ID, id);
            final IssueDetailFragment fragment = new IssueDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.issue_detail_container, fragment)
                    .commit();

        } else {
            // TODO change to AA
            final Intent detailIntent = new Intent(this, IssueDetailActivity.class);
            detailIntent.putExtra(IssueDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }
}
