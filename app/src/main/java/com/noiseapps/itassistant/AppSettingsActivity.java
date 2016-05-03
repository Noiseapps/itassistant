package com.noiseapps.itassistant;

import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.annimon.stream.Stream;
import com.noiseapps.itassistant.connector.JiraConnector;
import com.noiseapps.itassistant.database.Preferences_;
import com.noiseapps.itassistant.database.dao.AccountsDao;
import com.noiseapps.itassistant.model.account.AccountTypes;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.jira.projects.JiraProject;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
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

    @AfterViews
    void init() {
        fetchJiraAccountList();
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

        final MaterialDialog dialog = new MaterialDialog.Builder(this).items(jiraProjects).
                itemsCallbackMultiChoice(indexArr, (dialog1, which, text) -> {
                    final Set<String> projects = new HashSet<>(which.length);
                    for (Integer integer : which) {
                        projects.add(jiraProjects.get(integer).getKey());
                    }
                    preferences.shownProjects().put(projects);
                    dialog1.dismiss();
                    return true;
                }).build();
        dialog.show();
    }

}
