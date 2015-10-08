package com.noiseapps.itassistant.fragment.accounts;


import com.noiseapps.itassistant.model.account.AccountTypes;

public interface AccountsActivityCallbacks {

    void onAddAccount();

    void onAccountTypeSelected(@AccountTypes.AccountType int accountType);

    void onAccountSaved();
}
