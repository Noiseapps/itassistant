package com.noiseapps.itassistant.fragment.accounts;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.noiseapps.itassistant.BuildConfig;
import com.noiseapps.itassistant.R;
import com.noiseapps.itassistant.connector.JiraConnector;
import com.noiseapps.itassistant.database.dao.AccountsDao;
import com.noiseapps.itassistant.model.account.AccountTypes;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.jira.user.JiraUser;
import com.noiseapps.itassistant.utils.AuthenticatedPicasso;
import com.noiseapps.itassistant.utils.ImageUtils;
import com.noiseapps.itassistant.utils.StringUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.EditorAction;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EFragment(R.layout.fragment_account_atlassian)
public class JiraAccountCreateFragment extends Fragment implements Validator.ValidationListener, DialogInterface.OnCancelListener {

    public static final String SEPARATOR = "_";
    public static final int TIMEOUT_DURATION = 45;
    @FragmentArg
    BaseAccount editAccount;
    @Bean
    JiraConnector connector;
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

    @NotEmpty
    @ViewById
    EditText accountName;

    @ViewById
    RelativeLayout rootView;
    @ViewById
    FloatingActionButton saveFab;
    private Validator validator;
    private MaterialDialog progressDialog;
    private boolean requestCanceled;
    private BaseAccount currentConfig;
    private Bitmap avatarBitmap;
    private AccountsActivityCallbacks callbacks;
    private Handler handler;

    @AfterViews
    void init() {
        setHasOptionsMenu(true);
        handler = new Handler();
        callbacks = (AccountsActivityCallbacks) getActivity();
        initToolbar();
        initData();
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    private void initData() {
        if(editAccount != null) {
            accountName.setText(editAccount.getName());
            host.setText(editAccount.getUrl());
            username.setText(editAccount.getUsername());
            password.setText(editAccount.getPassword());
            return;
        }
        if (BuildConfig.DEBUG) {
//            accountName.setText("Local");
            accountName.setText("Exaco");
//            host.setText("10.1.221.123:8080");
            host.setText("jira.exaco.pl");
//            username.setText("noiseapps@gmail.com");
            username.setText("tomasz.scibiorek");
//            password.setText("test123");
            password.setText("kotek77@");
        }
    }

    void saveAccount() {
        int id = getAccountId();
        final String avatarFilename = AccountTypes.getAccountName(AccountTypes.ACC_JIRA) + SEPARATOR + id + SEPARATOR + currentConfig.getUsername() + "_avatar.png";
        final String path = imageUtils.saveAvatar(avatarBitmap, avatarFilename);
        currentConfig.setAvatarPath(path);
        accountsDao.addOrUpdate(currentConfig);
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
            actionBar.setTitle(R.string.addJiraAccount);
            if(editAccount != null) {
                actionBar.setTitle(R.string.editJiraAccount);
            }

            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Click(R.id.saveFab)
    void onSaveClicked() {
        requestCanceled = false;
        handler.removeCallbacksAndMessages(null);
        startTimeout();
        showProgress();
        validator.validate(true);
    }

    private void startTimeout() {
        handler.postDelayed(() -> {
            requestCanceled = true;
            hideProgress();
            Snackbar.make(saveFab, R.string.requestTimedOut, Snackbar.LENGTH_LONG).show();
        }, TimeUnit.SECONDS.toMillis(TIMEOUT_DURATION));
    }

    private void showProgress() {
        progressDialog = new MaterialDialog.Builder(getActivity()).
                title(R.string.validatingForm).
                progress(true, 0).
                cancelable(true).
                cancelListener(this).
                show();


//        progressDialog = new ProgressDialog(getContext());
//        progressDialog.setIndeterminate(true);
//        progressDialog.setCancelable(true);
//        progressDialog.setCanceledOnTouchOutside(true);
//        progressDialog.setOnCancelListener(this);
//        progressDialog.setTitle(getString(R.string.validatingForm));
//        progressDialog.show();
    }

    @EditorAction(R.id.password)
    void onEditorActionsOnSomeTextViews(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            onSaveClicked();
        }
    }

    @Override
    public void onValidationSucceeded() {
        String host = this.host.getText().toString();
        if (!StringUtils.validUrl(host)) {
            host = "http://" + host;
        }
        final String accountName = this.accountName.getText().toString();
        final String username = this.username.getText().toString();
        final String password = this.password.getText().toString();
        progressDialog.setTitle(getString(R.string.loggingIn));
        int id = getAccountId();
        currentConfig = new BaseAccount(id, username, accountName, password, host, "", AccountTypes.ACC_JIRA);
        if (editAccount == null && existsInDb()) {
            Snackbar.make(saveFab, R.string.configExists, Snackbar.LENGTH_LONG).show();
            hideProgress();
            return;
        }
        connector.setCurrentConfig(currentConfig);
        AuthenticatedPicasso.setConfig(getActivity(), currentConfig);
        getUserData();
    }

    private int getAccountId() {
        int id;
        if(editAccount == null) {
            id = accountsDao.getNextId();
        } else {
            id = editAccount.getId();
        }
        return id;
    }

    void getUserData() {
        connector.getUserData().subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(this::onDataLoaded, throwable -> onDataLoaded(null));
    }

    void onDataLoaded(JiraUser userData) {
        if (userData != null & !requestCanceled) {
            final Picasso authPicasso = AuthenticatedPicasso.getAuthPicasso(getContext(), currentConfig);
            authPicasso.load(userData.getAvatarUrls().getAvatar48()).placeholder(R.drawable.ic_action_account_circle).into(new LoadTarget());
        } else {
            hideProgress();
            Snackbar.make(saveFab, R.string.couldNotFetchUserData, Snackbar.LENGTH_LONG).show();
        }
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

    class LoadTarget implements Target {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            avatarBitmap = bitmap;
            hideProgress();
            saveAccount();
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            hideProgress();
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    }
}
