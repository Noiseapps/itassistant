package com.noiseapps.itassistant.fragment.accounts;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@EFragment(R.layout.fragment_accounts)
public class AccountsListFragment extends Fragment {
    @ViewById
    Toolbar toolbar;
    @ViewById
    NestedScrollView emptyContainer;
    @Bean
    AccountsDao accountsDao;
    @ViewById
    RecyclerView list;
    private List<BaseAccount> accounts;
    private AccountsActivityCallbacks callbacks;

    @AfterViews
    void init() {
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

    @OptionsItem(android.R.id.home)
    void onHomePressed() {
        getActivity().onBackPressed();
    }

    @Click({R.id.addAccountFab, R.id.addAccount})
    void addAccount() {
        callbacks.onAddAccount();
    }

    private void prepareAdapter() {
        final AccountListAdapter listAdapter = new AccountListAdapter(getContext(), accounts);
        list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        list.setAdapter(listAdapter);
    }

    private void readAllAccounts() {
        accounts = accountsDao.getAll();
        setListVisibility();
        sortAccountList();
    }

    private void sortAccountList() {
        Collections.sort(accounts, new Comparator<BaseAccount>() {
            @Override
            public int compare(BaseAccount lhs, BaseAccount rhs) {
                return lhs.getAccountType() - rhs.getAccountType();
            }
        });
    }

    private void setListVisibility() {
        if(accounts.isEmpty()) {
            list.setVisibility(View.GONE);
            emptyContainer.setVisibility(View.VISIBLE);
        } else {
            list.setVisibility(View.VISIBLE);
            emptyContainer.setVisibility(View.GONE);
        }
    }

}
