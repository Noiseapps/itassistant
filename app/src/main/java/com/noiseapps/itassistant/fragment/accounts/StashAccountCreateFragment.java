package com.noiseapps.itassistant.fragment.accounts;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.connector.StashConnector;
import com.noiseapps.itassistant.database.dao.AccountsDao;
import com.noiseapps.itassistant.model.account.AccountTypes;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.stash.projects.UserProjects;
import com.noiseapps.itassistant.utils.ImageUtils;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@EFragment(R.layout.fragment_account_atlassian)
public class StashAccountCreateFragment extends Fragment implements Validator.ValidationListener, DialogInterface.OnCancelListener {

    public static final String SEPARATOR = "_";
    public static final int TIMEOUT_DURATION = 45;
    @FragmentArg
    int editId = -1;
    @Bean
    StashConnector connector;
    @Bean
    AccountsDao accountsDao;
    @Bean
    ImageUtils imageUtils;

    @ViewById
    Toolbar toolbar;

    @NotEmpty
    @ViewById
    EditText host;

    @NotEmpty
    @ViewById
    EditText username;

    @NotEmpty
    @ViewById
    EditText password;

    @ViewById
    RelativeLayout rootView;
    @ViewById
    FloatingActionButton saveFab;
    private Validator validator;
    private ProgressDialog progressDialog;
    private boolean requestCanceled;
    private BaseAccount currentConfig;
    private AccountsActivityCallbacks callbacks;
    private Handler handler;

    @AfterViews
    void init() {
        handler = new Handler();
        callbacks = (AccountsActivityCallbacks) getActivity();
        initToolbar();
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    void saveAccount() {
        int id = accountsDao.getNextId();
        final String avatarFilename = AccountTypes.getAccountName(AccountTypes.ACC_STASH) + SEPARATOR + id + SEPARATOR + currentConfig.getUsername() + "_avatar.png";
        final String path = imageUtils.saveAvatar(BitmapFactory.decodeResource(getResources(), R.drawable.stash), avatarFilename);
        accountsDao.add(new BaseAccount(id, currentConfig.getUsername(), currentConfig.getPassword(), currentConfig.getUrl(), path, AccountTypes.ACC_STASH));
        handler.removeCallbacksAndMessages(null);
        callbacks.onAccountSaved();
    }

    @OptionsItem(android.R.id.home)
    void onHome() {
        getActivity().onBackPressed();
    }

    private void initToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(getContext(), R.color.white));
        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {

            actionBar.setTitle(R.string.addStashAccount);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Click(R.id.saveFab)
    void onVerify() {
        requestCanceled = false;
        handler.removeCallbacksAndMessages(null);
        startTimeout();
        showProgress();
        validator.validate(true);
    }

    private void startTimeout() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                requestCanceled = true;
                hideProgress();
                Snackbar.make(saveFab, R.string.requestTimedOut, Snackbar.LENGTH_LONG).show();
            }
        }, TimeUnit.SECONDS.toMillis(TIMEOUT_DURATION));
    }

    private void showProgress() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setOnCancelListener(this);
        progressDialog.setTitle(getString(R.string.validatingForm));
        progressDialog.show();
    }

    @EditorAction(R.id.password)
    void onEditorActionsOnSomeTextViews(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            onVerify();
        }
    }

    @Override
    public void onValidationSucceeded() {
        final String host = this.host.getText().toString();
        final String username = this.username.getText().toString();
        final String password = this.password.getText().toString();
        progressDialog.setTitle(getString(R.string.loggingIn));
        currentConfig = new BaseAccount(accountsDao.getNextId(), username, password, host, "", AccountTypes.ACC_STASH);
        if (existsInDb()) {
            Snackbar.make(saveFab, R.string.configExists, Snackbar.LENGTH_LONG).show();
            return;
        }
        connector.setCurrentConfig(currentConfig);
        connector.getProjects(new ProjectsCallback());
    }

    private boolean existsInDb() {
        for (BaseAccount account : accountsDao.getAll()) {
            if (account.getUsername().equals(currentConfig.getUsername())
                    && account.getUrl().equals(currentConfig.getUrl())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            ((EditText) error.getView()).setError(error.getCollatedErrorMessage(getContext()));
        }
        hideProgress();
    }

    private void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        validator.cancelAsync();
        requestCanceled = true;
        Snackbar.make(saveFab, R.string.userCanceledRequest, Snackbar.LENGTH_LONG).show();
    }

    private class ProjectsCallback implements Callback<UserProjects> {
        @Override
        public void success(UserProjects userProjects, Response response) {
            Logger.w(userProjects.toString());
            hideProgress();
            saveAccount();
        }

        @Override
        public void failure(RetrofitError error) {
            hideProgress();
            Snackbar.make(saveFab, R.string.invalidCredentials, Snackbar.LENGTH_LONG).show();
        }
    }
}
