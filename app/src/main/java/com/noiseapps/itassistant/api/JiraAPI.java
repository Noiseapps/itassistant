package com.noiseapps.itassistant.api;


import android.support.annotation.NonNull;

import com.noiseapps.itassistant.model.jira.issues.JiraIssue;
import com.noiseapps.itassistant.model.jira.projects.JiraProject;
import com.noiseapps.itassistant.model.jira.session.SessionRequest;
import com.noiseapps.itassistant.model.jira.session.SessionResponse;
import com.noiseapps.itassistant.model.jira.user.JiraUser;

import java.util.List;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public interface JiraAPI {

    @POST("/rest/auth/1/session")
    void newSession(@Body SessionRequest user, @NonNull Callback<SessionResponse> callback);

    @GET("/rest/api/2/user?expand=groups")
    void getUserData(@Query("username") String username, @NonNull Callback<JiraUser> callback);

    @GET("/rest/api/2/project")
    void getUserProjects(@NonNull Callback<List<JiraProject>> callback);

    @GET("/rest/api/2/search?maxResults=150&expand=transitions")
    void getProjectIssues(@Query("jql") String projectId, @NonNull Callback<JiraIssue> callback);
}
