package com.noiseapps.itassistant;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.noiseapps.itassistant.connector.JiraConnector;
import com.noiseapps.itassistant.database.Preferences_;
import com.noiseapps.itassistant.database.dao.AccountsDao;
import com.noiseapps.itassistant.model.account.AccountTypes;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.atlassian.AbstractBaseProject;
import com.noiseapps.itassistant.model.jira.projects.JiraProject;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@EActivity(R.layout.activity_app_settings)
public class AppSettingsActivity extends AppCompatActivity {

    final List<JiraProject> jiraProjects = new ArrayList<>();
    @Bean
    AccountsDao accountsDao;
    @Bean
    JiraConnector jiraConnector;
    @Pref
    Preferences_ preferences;
    @ViewById
    Toolbar toolbar;

    @AfterViews
    void init() {
        fetchJiraAccountList();
        initToolbar();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        final ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setHomeButtonEnabled(true);
            supportActionBar.setTitle(R.string.title_activity_settings);
        }
    }

    @OptionsItem(android.R.id.home)
    void onHomePressed() {
        onBackPressed();
    }

    @Background
    void fetchJiraAccountList() {
        Stream.of(accountsDao.getAll()).
                filter(account -> account.getAccountType() == AccountTypes.ACC_JIRA).
                forEach(this::fetchJiraAccountInfo);
    }

    private void fetchJiraAccountInfo(BaseAccount baseAccount) {
        try {
            jiraConnector.setCurrentConfig(baseAccount);
            final List<JiraProject> jiraProjects = jiraConnector.getUserProjects();
            this.jiraProjects.addAll(jiraProjects);
        } catch (Exception e) {
            Logger.e(e, e.getMessage());
        }
    }

    @Click(R.id.shownProjects)
    void onShowProjectList() {
        if (jiraProjects.isEmpty()) return;
        final List<Integer> indices = new ArrayList<>();
        final Set<String> shownProjects = preferences.shownProjects().get();
        for (int i = 0; i < jiraProjects.size(); i++) {
            final JiraProject project = jiraProjects.get(i);
            if (shownProjects.contains(project.getKey())) {
                indices.add(i);
            }
        }
        Integer[] indexArr = new Integer[indices.size()];
        indexArr = indices.toArray(indexArr);

        final List<String> list = Stream.of(jiraProjects).map(AbstractBaseProject::getName).collect(Collectors.toList());
        final MaterialDialog dialog = new MaterialDialog.Builder(this).
                items(list).
                itemsCallbackMultiChoice(indexArr, (dialog1, which, text) -> {
                    final Set<String> projects = new HashSet<>(which.length);
                    for (Integer integer : which) {
                        projects.add(jiraProjects.get(integer).getKey());
                    }
                    preferences.shownProjects().put(projects);
                    dialog1.dismiss();
                    setResult(RESULT_OK);
                    return true;
                }).positiveText(R.string.ok).onPositive((dialog1, which) -> dialog1.dismiss()).build();
        dialog.show();
    }

}
