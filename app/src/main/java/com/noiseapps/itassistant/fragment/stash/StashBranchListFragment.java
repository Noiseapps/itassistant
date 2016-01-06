package com.noiseapps.itassistant.fragment.stash;


import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.adapters.stash.BranchListAdapter;
import com.noiseapps.itassistant.connector.StashConnector;
import com.noiseapps.itassistant.model.stash.projects.BranchModel;
import com.noiseapps.itassistant.model.stash.projects.StashProject;
import com.noiseapps.itassistant.utils.DividerItemDecoration;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EFragment(R.layout.fragment_stash_branch_list)
public class StashBranchListFragment extends Fragment {

    @Bean
    StashConnector connector;

    @FragmentArg
    String repoSlug;
    @FragmentArg
    StashProject project;
    @ViewById
    RecyclerView branchesList;
    @ViewById
    View fetchingDataProgress;
    private List<BranchModel> branches;
    private BranchListAdapter listAdapter;
    private RecyclerViewTouchActionGuardManager recyclerViewTouchActionGuardManager;
    private RecyclerViewSwipeManager recyclerViewSwipeManager;
    private RecyclerView.Adapter wrappedAdapter;
    private BranchListAdapter.BranchListCallbacks listCallbacks = new BranchListAdapter.BranchListCallbacks() {
        @Override
        public void onItemClicked(BranchModel branchModel) {
            Logger.d("Item " + branchModel + "clicked");
        }

        @Override
        public void onItemRemoved(BranchModel branchModel) {
            Logger.d("Item " + branchModel + "removed");
        }
    };

    @AfterViews
    void init() {
        setupToolbar();
        showProgress();
        getBranches();
    }

    private void setupToolbar() {
        final ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setHomeButtonEnabled(true);
            supportActionBar.setTitle(getString(R.string.branchesForProject, project.getName()));
            setHasOptionsMenu(true);
        }
    }

    @OptionsItem(android.R.id.home)
    void onHomeSelected() {
        getActivity().onBackPressed();
    }

    @Background
    void getBranches() {
        final List<BranchModel> branches = connector.getBranches(project.getKey(), repoSlug);
        displayResult(branches);
    }

    @UiThread
    void displayResult(List<BranchModel> branches) {
        hideProgress();
        if (branches == null) {
            showError();
        } else {
            this.branches = branches;
            initList();
        }
    }

    private void initList() {
        listAdapter = new BranchListAdapter(getContext(), branches, listCallbacks);
        recyclerViewTouchActionGuardManager = new RecyclerViewTouchActionGuardManager();
        recyclerViewTouchActionGuardManager.setInterceptVerticalScrollingWhileAnimationRunning(true);
        recyclerViewTouchActionGuardManager.setEnabled(true);
        recyclerViewSwipeManager = new RecyclerViewSwipeManager();
        wrappedAdapter = recyclerViewSwipeManager.createWrappedAdapter(listAdapter);

        branchesList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        branchesList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        branchesList.setAdapter(wrappedAdapter);
        recyclerViewTouchActionGuardManager.attachRecyclerView(branchesList);
        recyclerViewSwipeManager.attachRecyclerView(branchesList);
    }

    @Override
    public void onDestroyView() {
        if (recyclerViewSwipeManager != null) {
            recyclerViewSwipeManager.release();
            recyclerViewSwipeManager = null;
        }

        if (recyclerViewTouchActionGuardManager != null) {
            recyclerViewTouchActionGuardManager.release();
            recyclerViewTouchActionGuardManager = null;
        }

        if (branchesList != null) {
            branchesList.setItemAnimator(null);
            branchesList.setAdapter(null);
            branchesList = null;
        }

        if (wrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(wrappedAdapter);
            wrappedAdapter = null;
        }
        super.onDestroyView();
    }

    private void showError() {
        Logger.e("Error");
    }

    private void showProgress() {
        fetchingDataProgress.setVisibility(View.VISIBLE);
        branchesList.setVisibility(View.GONE);
    }

    private void hideProgress() {
        fetchingDataProgress.setVisibility(View.GONE);
        branchesList.setVisibility(View.VISIBLE);
    }

}
