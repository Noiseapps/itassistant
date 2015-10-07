package com.noiseapps.itassistant.connector;

import android.support.annotation.NonNull;
import android.util.Base64;

import java.util.List;

import com.noiseapps.itassistant.api.JiraAPI;
import com.noiseapps.itassistant.database.PreferencesDAO;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.jira.issues.JiraIssue;
import com.noiseapps.itassistant.model.jira.issues.comments.Comment;
import com.noiseapps.itassistant.model.jira.issues.comments.Comments;
import com.noiseapps.itassistant.model.jira.issues.worklog.WorkLogItem;
import com.noiseapps.itassistant.model.jira.issues.worklog.WorkLogs;
import com.noiseapps.itassistant.model.jira.projects.JiraProject;
import com.noiseapps.itassistant.model.jira.user.JiraUser;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import retrofit.Callback;
import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

@EBean(scope = EBean.Scope.Singleton)
public class JiraConnector {

    @Bean
    PreferencesDAO preferencesDAO;

    private BaseAccount currentConfig;
    private JiraAPI apiService;

    public void getUserData(Callback<JiraUser> callback) {
        if(apiService == null) {
            return;
        }
        apiService.getUserData(currentConfig.getUsername(), callback);
    }

    public void getUserProjects(Callback<List<JiraProject>> callback) {
        if(apiService == null) {
            return;
        }
        apiService.getUserProjects(callback);
    }

    public void getProjectIssues(@NonNull String projectKey, Callback<JiraIssue> callback) {
        if(apiService == null) {
            return;
        }
        apiService.getProjectIssues(String.format("project=\"%s\"", projectKey), callback);
    }

    public void getIssueComments(@NonNull String issueId, Callback<Comments> callback) {
        if(apiService == null) {
            return;
        }
        apiService.getIssueComments(issueId, callback);
    }

    public void postIssueComment(String issueId, Comment comment, Callback<Comment> callback){
        if(apiService == null) {
            return;
        }
        apiService.addIssueComment(issueId, comment, callback);
    }

    public void getIssueWorkLog(@NonNull String issueId, Callback<WorkLogs> callback) {
        if(apiService == null) {
            return;
        }
        apiService.getIssueWorkLog(issueId, callback);
    }

    public void postIssueWorkLog(String issueId, String newEstimate, WorkLogItem workLog, Callback<WorkLogItem> callback){
        if(apiService == null) {
            return;
        }
        apiService.postIssueWorkLog(issueId, newEstimate, workLog, callback);
    }

    @AfterInject
    void init() {
        initApiService();
    }

    private void initApiService() {
        if(currentConfig == null) {
            return;
        }
        final RestAdapter adapter = new RestAdapter.Builder().
                setRequestInterceptor(new Interceptor()).
                setLogLevel(RestAdapter.LogLevel.FULL).
                setErrorHandler(new JiraErrorHandler()).
                setEndpoint(currentConfig.getUrl()).build();
        apiService = adapter.create(JiraAPI.class);
    }

    public void setCurrentConfig(BaseAccount newConfig) {
        currentConfig = newConfig;
        initApiService();
    }

    private String getBasicAuth() {
        String usernameString = currentConfig.getUsername() + ":" + currentConfig.getPassword();
        return Base64.encodeToString(usernameString.getBytes(), Base64.DEFAULT).replaceAll("\n", "");
    }

    private class JiraErrorHandler implements ErrorHandler {

        @Override
        public Throwable handleError(RetrofitError cause) {
            Logger.e(cause, cause.getMessage());
            final Response response = cause.getResponse();
            if (response != null) {
                switch (response.getStatus()) {
                    case Consts.HTTP_NOT_AUTHORIZED:
                        break;
                    case Consts.HTTP_FORBIDDEN:
                        break;
                    case Consts.HTTP_SERVER_ERROR:
                        break;
                    default:
                }
            }
            return cause;
        }
    }

    private class Interceptor implements RequestInterceptor {

        @Override
        public void intercept(RequestFacade request) {
            addAuthenticationHeader(request);
        }

        private void addAuthenticationHeader(RequestFacade request) {
            final String basicAuthEncoded = getBasicAuth();
            request.addHeader(Consts.AUTH_HEADER, String.format(Consts.AUTH_HEADER_VALUE, basicAuthEncoded));
        }
    }

}
