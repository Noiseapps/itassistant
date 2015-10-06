package com.noiseapps.itassistant;

import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.noiseapps.itassistant.fragment.accounts.AccountTypeSelectFragment_;
import com.noiseapps.itassistant.fragment.accounts.AccountsActivityCallbacks;
import com.noiseapps.itassistant.fragment.accounts.AccountsListFragment_;
import com.noiseapps.itassistant.fragment.accounts.JiraAccountCreateFragment_;
import com.noiseapps.itassistant.fragment.accounts.StashAccountCreateFragment_;
import com.noiseapps.itassistant.model.account.AccountTypes;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_accounts)
public class AccountsActivity extends AppCompatActivity implements AccountsActivityCallbacks {

    @ViewById
    FrameLayout container;

    @Override
    public void onAddAccount() {
        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.container, AccountTypeSelectFragment_.builder().build()).commit();
    }

    @Override
    public void onAccountTypeSelected(@AccountTypes.AccountType int accountType) {
        if (accountType == AccountTypes.ACC_JIRA) {
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.container, JiraAccountCreateFragment_.builder().build()).commit();
        } else if (accountType == AccountTypes.ACC_STASH) {
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.container, StashAccountCreateFragment_.builder().build()).commit();
        }
    }

    @Override
    public void onAccountSaved() {
        clearBackstack();
        init();
        setResult(RESULT_OK);
        Snackbar.make(container, R.string.accountSaved, Snackbar.LENGTH_LONG).show();
    }

    private void clearBackstack() {
        final FragmentManager supportFragmentManager = getSupportFragmentManager();
        int stackCount = supportFragmentManager.getBackStackEntryCount();
        for (int i = 0; i < stackCount; i++) {
            supportFragmentManager.popBackStack();
        }
    }

    @AfterViews
    void init() {
        setResult(RESULT_CANCELED);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, AccountsListFragment_.builder().build()).commit();
    }
}
