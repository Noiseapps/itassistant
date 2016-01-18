package com.noiseapps.itassistant.api;

import com.noiseapps.itassistant.model.atlassian.PagedApiModel;
import com.noiseapps.itassistant.model.stash.projects.BranchModel;
import com.noiseapps.itassistant.model.stash.projects.NewBranchModel;
import com.noiseapps.itassistant.model.stash.projects.ProjectRepos;
import com.noiseapps.itassistant.model.stash.projects.UserProjects;
import com.noiseapps.itassistant.utils.annotations.DELETEBODY;

import java.util.Map;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface StashAPI {

    @GET("/rest/api/1.0/projects")
    UserProjects getProjects();

    @GET("/rest/api/1.0/projects")
    Observable<UserProjects> reactiveGetProjects();

    @GET("/rest/api/1.0/projects/{projectKey}/repos")
    Observable<ProjectRepos> getProjectRepos(@Path("projectKey") String projectKey);

    @GET("/rest/api/1.0/projects/{projectKey}/repos/{repoSlug}")
    Observable<Object> getRepoDetails(@Path("projectKey") String projectKey,
                                      @Path("repoSlug") String repoSlug);

    @GET("/rest/api/1.0/projects/{projectKey}/repos/{repoSlug}/branches?orderBy=MODIFICATION&details=true")
    PagedApiModel<BranchModel> getBranches(@Path("projectKey") String projectKey,
                                           @Path("repoSlug") String repoSlug,
                                           @Query("start") int start);

    @POST("/rest/branch-utils/1.0/projects/{projectKey}/repos/{repoSlug}/branches")
    Observable<BranchModel> createBranch(@Path("projectKey") String projectKey,
                                         @Path("repoSlug") String repoSlug,
                                         @Body NewBranchModel newBranchModel);

    @DELETEBODY("/rest/branch-utils/1.0/projects/{projectKey}/repos/{repoSlug}/branches")
    Observable<Response> deleteBranch(@Path("projectKey") String projectKey,
                                      @Path("repoSlug") String repoSlug,
                                      @Body Map<String, Object> params);

}
