package com.noiseapps.itassistant.fragment.accounts;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.adapters.AccountTypeListAdapter;
import com.noiseapps.itassistant.model.account.AccountTypes;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;


@EFragment(R.layout.fragment_account_type_select)
public class AccountTypeSelectFragment extends Fragment {

    @ViewById
    ListView listView;
    @ViewById
    Toolbar toolbar;
    private AccountsActivityCallbacks callbacks;

    @AfterViews
    void init() {
        setHasOptionsMenu(true);
        callbacks = (AccountsActivityCallbacks) getActivity();
        final String[] values = getResources().getStringArray(R.array.accountTypes);
        final AccountTypeListAdapter adapter = new AccountTypeListAdapter(getContext(), values, new Callbacks());
        listView.setAdapter(adapter);
        initToolbar();
    }

    private void initToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(getContext(), R.color.white));
        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.selectAccountType);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @OptionsItem(android.R.id.home)
    void onHome() {
        getActivity().onBackPressed();
    }

    private class Callbacks implements AccountTypeListAdapter.AccountTypeListener {
        @Override
        public void onAccountTypeSelected(@AccountTypes.AccountType int accountType) {
            callbacks.onAccountTypeSelected(accountType);
        }
    }
}
