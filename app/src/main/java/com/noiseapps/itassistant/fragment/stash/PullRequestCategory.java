package com.noiseapps.itassistant.fragment.stash;


import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.adapters.stash.PrListAdapter;
import com.noiseapps.itassistant.model.stash.pullrequests.PullRequest;
import com.noiseapps.itassistant.utils.DividerItemDecoration;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EFragment(R.layout.fragment_pr_tab)
public class PullRequestCategory extends Fragment {

    @FragmentArg
    ArrayList<PullRequest> pullRequests;

    @ViewById
    RecyclerView prList;

    @ViewById
    TextView noPullRequests;
    private Activity activity;

    @AfterViews
    void init() {
        activity = getActivity();
        final PrListAdapter adapter = new PrListAdapter(activity, pullRequests, pullRequest -> {
            Logger.d(pullRequest.toString());
        });
        prList.setLayoutManager(new LinearLayoutManager(activity));
        prList.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL_LIST));
        prList.setHasFixedSize(true);
        prList.setAdapter(adapter);

        if (pullRequests.isEmpty()) {
            noPullRequests.setVisibility(View.VISIBLE);
            prList.setVisibility(View.GONE);
        } else {
            prList.setVisibility(View.VISIBLE);
            noPullRequests.setVisibility(View.GONE);
        }
    }

}
