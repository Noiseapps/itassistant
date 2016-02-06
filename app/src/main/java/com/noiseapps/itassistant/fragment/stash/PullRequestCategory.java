package com.noiseapps.itassistant.fragment.stash;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.adapters.stash.PrListAdapter;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.stash.pullrequests.PullRequest;
import com.noiseapps.itassistant.utils.AuthenticatedPicasso;
import com.noiseapps.itassistant.utils.DividerItemDecoration;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EFragment(R.layout.fragment_pr_tab)
public class PullRequestCategory extends Fragment {

    @FragmentArg
    ArrayList<PullRequest> pullRequests;
    @FragmentArg
    BaseAccount baseAccount;

    @ViewById
    RecyclerView prList;

    @ViewById
    TextView noPullRequests;
    private Activity activity;

    @AfterViews
    void init() {
        activity = getActivity();
        final Picasso authPicasso = AuthenticatedPicasso.getAuthPicasso(getActivity(), baseAccount);
        final PrListAdapter adapter = new PrListAdapter(activity, pullRequests, authPicasso, this::openPullRequestExternal);
        prList.setLayoutManager(new LinearLayoutManager(activity));
        prList.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL_LIST));
        prList.setHasFixedSize(true);
        prList.setAdapter(adapter);
        setListVisibility();
    }

    private void setListVisibility() {
        if (pullRequests.isEmpty()) {
            noPullRequests.setVisibility(View.VISIBLE);
            prList.setVisibility(View.GONE);
        } else {
            prList.setVisibility(View.VISIBLE);
            noPullRequests.setVisibility(View.GONE);
        }
    }

    private void openPullRequestExternal(PullRequest pullRequest) {
        final Intent showPrIntent = new Intent();
        showPrIntent.setAction(Intent.ACTION_VIEW);
        showPrIntent.setData(Uri.parse(pullRequest.getLinks().getSelf().get(0).getHref()));
        activity.startActivity(showPrIntent);
    }

    public void setPullRequests(ArrayList<PullRequest> pullRequests) {
        this.pullRequests = pullRequests;
        prList.getAdapter().notifyDataSetChanged();
        setListVisibility();
    }
}
