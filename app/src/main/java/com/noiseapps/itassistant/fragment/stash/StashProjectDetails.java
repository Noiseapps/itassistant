package com.noiseapps.itassistant.fragment.stash;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.model.stash.projects.StashProject;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_stash_project)
public class StashProjectDetails extends Fragment {

    @ViewById
    TextView textView3;

    @FragmentArg
    StashProject stashProject;

    @AfterViews
    void init() {
        Logger.d(String.valueOf(stashProject));
        final ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setCustomView(R.layout.layout_toolbar_spinner);
            supportActionBar.setDisplayShowCustomEnabled(true);
        }

    }

    @Override
    public void onDetach() {
        final ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowCustomEnabled(false);
        }
        super.onDetach();
    }
}
