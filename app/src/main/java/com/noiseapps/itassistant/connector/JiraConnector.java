package com.noiseapps.itassistant.connector;

import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Pair;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.noiseapps.itassistant.api.JiraAPI;
import com.noiseapps.itassistant.database.PreferencesDAO;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.jira.issues.Assignee;
import com.noiseapps.itassistant.model.jira.issues.Issue;
import com.noiseapps.itassistant.model.jira.issues.JiraIssueList;
import com.noiseapps.itassistant.model.jira.issues.Transition;
import com.noiseapps.itassistant.model.jira.issues.TransitionRequest;
import com.noiseapps.itassistant.model.jira.issues.comments.Comment;
import com.noiseapps.itassistant.model.jira.issues.comments.Comments;
import com.noiseapps.itassistant.model.jira.issues.common.IssueStatus;
import com.noiseapps.itassistant.model.jira.projects.createissue.CreateIssueModel;
import com.noiseapps.itassistant.model.jira.projects.createissue.CreateIssueResponse;
import com.noiseapps.itassistant.model.jira.projects.createmeta.CreateMetaModel;
import com.noiseapps.itassistant.model.jira.projects.details.JiraProjectDetails;
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
import rx.Observable;

@EBean(scope = EBean.Scope.Singleton)
public class JiraConnector {

    @Bean
    PreferencesDAO preferencesDAO;

    private BaseAccount currentConfig;
    private JiraAPI apiService;

    public Observable<JiraUser> getUserData() {
        if(apiService == null) {
            return null;
        }
        return apiService.getUserData(currentConfig.getUsername());
    }

    public List<JiraProject> getUserProjects() {
        if (apiService == null) {
            return null;
        }
        try {
            return apiService.getUserProjects();
        } catch (RetrofitError error) {
            return null;
        }
    }

    public JiraIssueList getAssignedToMe() {
        if (apiService == null) {
            return null;
        }
        try {
            final String query = String.format("assignee=\"%s\"", getCurrentConfig().getUsername());
            return apiService.getAssignedToMe(query);
        } catch (RetrofitError error) {
            return null;
        }
    }

//    public void getProjectIssues(@NonNull String projectKey, Callback<JiraIssueList> callback) {
//        if(apiService == null) {
//            return;
//        }
//        apiService.getProjectIssues(String.format("project=\"%s\"", projectKey), callback);
//    }

    public Observable<JiraIssueList> getProjectIssues(@NonNull String projectKey) {
        if(apiService == null) {
            return null;
        }
        return apiService.getProjectIssues(String.format("project=\"%s\"", projectKey));
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

    public Observable<JiraProjectDetails> getProjectDetails(@NonNull String projectId) {
        if(apiService == null) {
            return null;
        }
        return apiService.getProjectDetails(projectId);
    }

    public void postIssueWorkLog(String issueId, String newEstimate, WorkLogItem workLog, Callback<WorkLogItem> callback){
        if(apiService == null) {
            return;
        }
        apiService.postIssueWorkLog(issueId, newEstimate, workLog, callback);
    }

    public void getProjectStatuses(@NonNull String issueId, Callback<List<IssueStatus>> callback) {
        if(apiService == null) {
            return;
        }
        apiService.getProjectStatuses(issueId, callback);
    }

    public Observable<List<Assignee>> getProjectMembers(@NonNull String projectKey) {
        if(apiService == null) {
            return null;
        }
        return apiService.getProjectMembers(projectKey);
    }

    public Observable<CreateMetaModel> getCreateMeta(@NonNull String projectKey) {
        if(apiService == null) {
            return null;
        }
        return apiService.getCreateMeta(projectKey);
    }

    public void postNewIssue(@NonNull CreateIssueModel issueModel, Callback<CreateIssueResponse> callback) {
        if(apiService == null) {
            return;
        }
        apiService.postNewIssue(issueModel, callback);
    }

    public void updateIssue(@NonNull String issueId, @NonNull CreateIssueModel issueModel, Callback<CreateIssueResponse> callback) {
        if(apiService == null) {
            return;
        }
        apiService.updateIssue(issueId, issueModel, callback);
    }

    public Response changeAssignee(@NonNull String projectKey, String username) {
        if(apiService == null) {
            return null;
        }
        try {
            return apiService.changeIssueAssignee(projectKey, Collections.singletonMap("name", username));
        } catch (RetrofitError err) {
            Logger.e(err, err.getMessage());
            return null;
        }
    }

    public Response transitionTo(Issue issue, Transition transition) {
        if(apiService == null) {
            return null;
        }
        try {
            return apiService.transitionTo(issue.getId(), new TransitionRequest(transition));
        } catch (RetrofitError err) {
            Logger.e(err, err.getMessage());
            return null;
        }
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

    public BaseAccount getCurrentConfig() {
        return currentConfig;
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
