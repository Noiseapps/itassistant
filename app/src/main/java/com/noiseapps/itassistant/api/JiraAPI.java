package com.noiseapps.itassistant.api;


import android.support.annotation.NonNull;

import java.util.List;

import com.noiseapps.itassistant.model.jira.issues.Assignee;
import com.noiseapps.itassistant.model.jira.issues.JiraIssue;
import com.noiseapps.itassistant.model.jira.issues.Priority;
import com.noiseapps.itassistant.model.jira.issues.comments.Comment;
import com.noiseapps.itassistant.model.jira.issues.comments.Comments;
import com.noiseapps.itassistant.model.jira.issues.common.IssueStatus;
import com.noiseapps.itassistant.model.jira.projects.details.JiraProjectDetails;
import com.noiseapps.itassistant.model.jira.issues.worklog.WorkLogItem;
import com.noiseapps.itassistant.model.jira.issues.worklog.WorkLogs;
import com.noiseapps.itassistant.model.jira.projects.JiraProject;
import com.noiseapps.itassistant.model.jira.session.SessionRequest;
import com.noiseapps.itassistant.model.jira.session.SessionResponse;
import com.noiseapps.itassistant.model.jira.user.JiraUser;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface JiraAPI {

    @POST("/rest/auth/1/session")
    void newSession(@Body SessionRequest user, @NonNull Callback<SessionResponse> callback);

    @GET("/rest/api/2/user?expand=groups")
    void getUserData(@Query("username") String username, @NonNull Callback<JiraUser> callback);

    @GET("/rest/api/2/project")
    void getUserProjects(@NonNull Callback<List<JiraProject>> callback);

    @GET("/rest/api/2/project/{projectIdOrKey}")
    void getProjectDetails(@Path("projectIdOrKey") String projectId, @NonNull Callback<JiraProjectDetails> callback);

    @GET("/rest/api/2/search?maxResults=150&expand=transitions")
    void getProjectIssues(@Query("jql") String projectId, @NonNull Callback<JiraIssue> callback);

    @GET("/rest/api/2/issue/{issueIdOrKey}/comment")
    void getIssueComments(@Path("issueIdOrKey") String issueId, @NonNull Callback<Comments> callback);

    @POST("/rest/api/2/issue/{issueIdOrKey}/comment")
    void addIssueComment(@Path("issueIdOrKey") String issueId, @Body Comment comment, @NonNull Callback<Comment> callback);

    @GET("/rest/api/2/issue/{issueIdOrKey}/worklog")
    void getIssueWorkLog(@Path("issueIdOrKey") String issueId, @NonNull Callback<WorkLogs> callback);

    @POST("/rest/api/2/issue/{issueIdOrKey}/worklog?adjustEstimate=new")
    void postIssueWorkLog(@Path("issueIdOrKey") String issueId, @Query("newEstimate") String newEstimate, @Body WorkLogItem comment, @NonNull Callback<WorkLogItem> callback);

    @GET("/rest/api/2/project/{projectIdOrKey}/statuses")
    void getProjectStatuses(@Path("projectIdOrKey") String projectId, @NonNull Callback<List<IssueStatus>> callback);

    @GET("/rest/api/2/user/assignable/search")
    void getProjectMembers(@Query("project") String projectKey, @NonNull Callback<List<Assignee>> callback);

    @GET("/rest/api/2/priority")
    void getIssuePriorities(@NonNull Callback<List<Priority>> callback);


}
