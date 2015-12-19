package com.noiseapps.itassistant.fragment.accounts;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.noiseapps.itassistant.AnalyticsTrackers;
import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.adapters.AccountListAdapter;
import com.noiseapps.itassistant.database.dao.AccountsDao;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.utils.DividerItemDecoration;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@EFragment(R.layout.fragment_accounts)
public class AccountsListFragment extends Fragment {
    private final List<BaseAccount> accounts = new ArrayList<>();
    @ViewById
    Toolbar toolbar;
    @ViewById
    LinearLayout emptyView;
    @Bean
    AccountsDao accountsDao;
    @ViewById(R.id.list)
    RecyclerView recyclerView;
    @Bean
    AnalyticsTrackers tracker;
    private AccountsActivityCallbacks callbacks;
    private RecyclerViewTouchActionGuardManager recyclerViewTouchActionGuardManager;
    private RecyclerViewSwipeManager recyclerViewSwipeManager;
    private RecyclerView.Adapter wrappedAdapter;
    private AccountListAdapter listAdapter;

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

        if (recyclerView != null) {
            recyclerView.setItemAnimator(null);
            recyclerView.setAdapter(null);
            recyclerView = null;
        }

        if (wrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(wrappedAdapter);
            wrappedAdapter = null;
        }
        super.onDestroyView();
    }

    @AfterViews
    void init() {
        setHasOptionsMenu(true);
        callbacks = (AccountsActivityCallbacks) getActivity();
        initToolbar();
        readAllAccounts();
        prepareAdapter();
    }

    private void initToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(getContext(), R.color.white));
        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.action_accounts);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    private void prepareAdapter() {
        listAdapter = new AccountListAdapter(getContext(), accounts, new AdapterCallbacks());
        recyclerViewTouchActionGuardManager = new RecyclerViewTouchActionGuardManager();
        recyclerViewTouchActionGuardManager.setInterceptVerticalScrollingWhileAnimationRunning(true);
        recyclerViewTouchActionGuardManager.setEnabled(true);
        recyclerViewSwipeManager = new RecyclerViewSwipeManager();
        wrappedAdapter = recyclerViewSwipeManager.createWrappedAdapter(listAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(wrappedAdapter);
        recyclerViewTouchActionGuardManager.attachRecyclerView(recyclerView);
        recyclerViewSwipeManager.attachRecyclerView(recyclerView);
    }

    private void readAllAccounts() {
        accounts.clear();
        accounts.addAll(accountsDao.getAll());
        setListVisibility();
        sortAccountList();
    }

    private void sortAccountList() {
        Collections.sort(accounts, this::compare);
    }

    private void setListVisibility() {
        if (accounts.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    @OptionsItem(android.R.id.home)
    void onHomePressed() {
        getActivity().onBackPressed();
    }

    @Click({R.id.addAccountFab, R.id.addAccount})
    void addAccount() {
        callbacks.onAddAccount();
    }

    private int compare(BaseAccount lhs, BaseAccount rhs) {
        return lhs.getAccountType() - rhs.getAccountType();
    }

    private void removeAccount(BaseAccount account) {
        tracker.sendEvent(AnalyticsTrackers.SCREEN_ACCOUNTS, AnalyticsTrackers.CATEGORY_ACCOUNTS, "accountRemoved");
        accountsDao.delete(account);
        readAllAccounts();
        listAdapter.notifyDataSetChanged();
        getActivity().setResult(Activity.RESULT_OK);
        Snackbar.make(recyclerView, R.string.removed, Snackbar.LENGTH_LONG).setAction(R.string.undo, v -> {
            restoreAccount(account);
        }).show();
    }

    private void restoreAccount(BaseAccount account) {
        tracker.sendEvent(AnalyticsTrackers.SCREEN_ACCOUNTS, AnalyticsTrackers.CATEGORY_ACCOUNTS, "accountRestored");
        accountsDao.add(account);
        readAllAccounts();
    }

    private class AdapterCallbacks implements AccountListAdapter.AccountListCallbacks {
        @Override
        public void onItemSelected(BaseAccount account) {
            callbacks.onEditAccount(account);
        }

        @Override
        public void onItemRemoved(final BaseAccount account) {
            removeAccount(account);
        }
    }
}
