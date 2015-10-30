package com.noiseapps.itassistant.fragment.accounts;


import com.noiseapps.itassistant.model.account.AccountTypes;
import com.noiseapps.itassistant.model.account.BaseAccount;

public interface AccountsActivityCallbacks {

    void onAddAccount();

    void onAccountTypeSelected(@AccountTypes.AccountType int accountType);

    void onAccountSaved();

    void onEditAccount(BaseAccount account);
}
