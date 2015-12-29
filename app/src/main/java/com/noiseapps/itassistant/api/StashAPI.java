package com.noiseapps.itassistant.api;

import com.noiseapps.itassistant.model.stash.projects.ProjectRepos;
import com.noiseapps.itassistant.model.stash.projects.UserProjects;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface StashAPI {

    @GET("/rest/api/1.0/projects")
    UserProjects getProjects();

    @GET("/rest/api/1.0/projects")
    Observable<UserProjects> reactiveGetProjects();

    @GET("/rest/api/1.0/projects/{projectKey}/repos")
    Observable<ProjectRepos> getProjectRepos(@Path("projectKey") String projectKey);

    @GET("/rest/api/1.0/projects/{projectKey}/repos/{repoSlug}")
    Observable<Object> getRepoDetails(@Path("projectKey") String projectKey, @Path("repoSlug") String repoSlug);

}
