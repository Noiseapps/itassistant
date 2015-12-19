package com.noiseapps.itassistant;

import android.content.pm.ActivityInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.noiseapps.itassistant.fragment.accounts.AccountsActivityCallbacks;
import com.noiseapps.itassistant.fragment.accounts.AccountsListFragment_;
import com.noiseapps.itassistant.fragment.accounts.JiraAccountCreateFragment;
import com.noiseapps.itassistant.fragment.accounts.JiraAccountCreateFragment_;
import com.noiseapps.itassistant.fragment.accounts.StashAccountCreateFragment_;
import com.noiseapps.itassistant.model.account.AccountTypes;
import com.noiseapps.itassistant.model.account.BaseAccount;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import static com.noiseapps.itassistant.AnalyticsTrackers.CATEGORY_ACCOUNTS;
import static com.noiseapps.itassistant.AnalyticsTrackers.CATEGORY_APP;
import static com.noiseapps.itassistant.AnalyticsTrackers.SCREEN_ACCOUNTS;

@EActivity(R.layout.activity_accounts)
public class AccountsActivity extends AppCompatActivity implements AccountsActivityCallbacks {

    @ViewById
    FrameLayout container;

    @Extra
    boolean showAccountForm;

    @Override
    public void onAddAccount() {
        onAccountTypeSelected(AccountTypes.ACC_JIRA);
        tracker.sendEvent(SCREEN_ACCOUNTS, CATEGORY_ACCOUNTS, "onAdd");
    }

    @Override
    public void onAccountTypeSelected(@AccountTypes.AccountType int accountType) {
        tracker.sendEvent(AnalyticsTrackers.SCREEN_ACCOUNTS, AnalyticsTrackers.CATEGORY_ACCOUNTS, "accountTypeSelected", String.valueOf(accountType));
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
        tracker.sendEvent(SCREEN_ACCOUNTS, CATEGORY_ACCOUNTS, "saved");
    }

    @Override
    public void onEditAccount(BaseAccount account) {
        final JiraAccountCreateFragment fragment = JiraAccountCreateFragment_.builder().editAccount(account).build();
        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.container, fragment).commit();
        tracker.sendEvent(SCREEN_ACCOUNTS, CATEGORY_ACCOUNTS, "onEdit");
    }


    private void clearBackstack() {
        final FragmentManager supportFragmentManager = getSupportFragmentManager();
        int stackCount = supportFragmentManager.getBackStackEntryCount();
        for (int i = 0; i < stackCount; i++) {
            supportFragmentManager.popBackStack();
        }
    }

    @Bean
    AnalyticsTrackers tracker;

    @AfterViews
    void init() {
        tracker.sendScreenVisit(SCREEN_ACCOUNTS);
        setTablet();
        setResult(RESULT_CANCELED);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, AccountsListFragment_.builder().build()).commit();

        if(showAccountForm) {
            onAddAccount();
            showAccountForm = false;
        }
    }

    private void setTablet() {
        final boolean isTabletScreenSize = getResources().getBoolean(R.bool.tabletSize);
        tracker.sendEvent(SCREEN_ACCOUNTS, CATEGORY_APP, "isTablet", String.valueOf(isTabletScreenSize));
        if (isTabletScreenSize) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
    }
}
