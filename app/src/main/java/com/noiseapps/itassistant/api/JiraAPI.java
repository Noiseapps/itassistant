package com.noiseapps.itassistant.api;


import android.support.annotation.NonNull;

import com.noiseapps.itassistant.model.jira.issues.Assignee;
import com.noiseapps.itassistant.model.jira.issues.Issue;
import com.noiseapps.itassistant.model.jira.issues.JiraIssueList;
import com.noiseapps.itassistant.model.jira.issues.TransitionRequest;
import com.noiseapps.itassistant.model.jira.issues.comments.Comment;
import com.noiseapps.itassistant.model.jira.issues.comments.Comments;
import com.noiseapps.itassistant.model.jira.issues.common.IssueStatus;
import com.noiseapps.itassistant.model.jira.issues.worklog.WorkLogItem;
import com.noiseapps.itassistant.model.jira.issues.worklog.WorkLogs;
import com.noiseapps.itassistant.model.jira.projects.JiraProject;
import com.noiseapps.itassistant.model.jira.projects.createissue.CreateIssueModel;
import com.noiseapps.itassistant.model.jira.projects.createissue.CreateIssueResponse;
import com.noiseapps.itassistant.model.jira.projects.createmeta.CreateMetaModel;
import com.noiseapps.itassistant.model.jira.projects.details.JiraProjectDetails;
import com.noiseapps.itassistant.model.jira.session.SessionRequest;
import com.noiseapps.itassistant.model.jira.session.SessionResponse;
import com.noiseapps.itassistant.model.jira.user.JiraUser;

import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface JiraAPI {

    @POST("/rest/auth/1/session")
    void newSession(@Body SessionRequest user, @NonNull Callback<SessionResponse> callback);

    @GET("/rest/api/2/user?expand=groups")
    Observable<JiraUser> getUserData(@Query("username") String username);

    @GET("/rest/api/2/project")
    List<JiraProject> getUserProjects();

    @GET("/rest/api/2/search")
    JiraIssueList getAssignedToMe(@Query("jql") String query, @Query("startAt") long startAt, @Query("expand") String expand);

    @GET("/rest/api/2/project/{projectIdOrKey}")
    Observable<JiraProjectDetails> getProjectDetails(@Path("projectIdOrKey") String projectId);

    @GET("/rest/api/2/search?maxResults=150&expand=transitions")
    JiraIssueList getProjectIssues(@Query("jql") String query, @Query("startAt") long startAt);

    @GET("/rest/api/2/issue/{issueIdOrKey}")
    Observable<Issue> getIssueDetails(@Path("issueIdOrKey") String issueId);

    @GET("/rest/api/2/issue/{issueIdOrKey}/comment")
    void getIssueComments(@Path("issueIdOrKey") String issueId, @NonNull Callback<Comments> callback);

    @POST("/rest/api/2/issue/{issueIdOrKey}/comment")
    Observable<Comment> addIssueComment(@Path("issueIdOrKey") String issueId, @Body Comment comment);

    @GET("/rest/api/2/issue/{issueIdOrKey}/worklog")
    WorkLogs getIssueWorkLog(@Path("issueIdOrKey") String issueId, @Query("startAt") long startAt);

    @POST("/rest/api/2/issue/{issueIdOrKey}/worklog?adjustEstimate=new")
    void postIssueWorkLog(@Path("issueIdOrKey") String issueId, @Query("newEstimate") String newEstimate, @Body WorkLogItem comment, @NonNull Callback<WorkLogItem> callback);

    @GET("/rest/api/2/project/{projectIdOrKey}/statuses")
    void getProjectStatuses(@Path("projectIdOrKey") String projectId, @NonNull Callback<List<IssueStatus>> callback);

    @GET("/rest/api/2/user/assignable/search")
    Observable<List<Assignee>> getProjectMembers(@Query("project") String projectKey);

    @GET("/rest/api/2/issue/createmeta?&expand=projects.issuetypes.fields")
    Observable<CreateMetaModel> getCreateMeta(@Query("projectKeys") String projectKey);

    @POST("/rest/api/2/issue")
    void postNewIssue(@Body CreateIssueModel createIssueModel, @NonNull Callback<CreateIssueResponse> callback);

    @PUT("/rest/api/2/issue/{issueId}")
    void updateIssue(@Path("issueId") String issueId, @Body CreateIssueModel createIssueModel, @NonNull Callback<CreateIssueResponse> callback);

    @POST("/rest/api/2/issue/{issueIdOrKey}/transitions")
    Response transitionTo(@Path("issueIdOrKey") String id, @Body TransitionRequest transition);

    @PUT("/rest/api/2/issue/{issueId}/assignee")
    Response changeIssueAssignee(@Path("issueId") String issueId, @Body Map<String, String> assignee);

}
