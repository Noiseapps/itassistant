package com.noiseapps.itassistant.fragment.stash;

import android.support.v4.app.Fragment;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.stash.pullrequests.details.DiffBase;
import com.noiseapps.itassistant.utils.AuthenticatedPicasso;
import com.noiseapps.itassistant.utils.views.DiffViewBase;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_pr_diff)
public class PullRequestDiffFragment extends Fragment {

    @ViewById
    DiffViewBase diffRoot;

    @FragmentArg
    DiffBase diffBase;

    @FragmentArg
    BaseAccount account;

    @AfterViews
    void init() {
        final Picasso picasso = AuthenticatedPicasso.getAuthPicasso(getContext(), account);
        diffRoot.setPicasso(picasso);
        diffRoot.setDiff(diffBase);
    }
}
