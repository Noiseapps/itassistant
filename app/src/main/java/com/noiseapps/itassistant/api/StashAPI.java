package com.noiseapps.itassistant.api;

import com.noiseapps.itassistant.model.atlassian.PagedApiModel;
import com.noiseapps.itassistant.model.stash.branches.BranchModel;
import com.noiseapps.itassistant.model.stash.commits.Commit;
import com.noiseapps.itassistant.model.stash.branches.NewBranchModel;
import com.noiseapps.itassistant.model.stash.general.ProjectRepos;
import com.noiseapps.itassistant.model.stash.general.StashUser;
import com.noiseapps.itassistant.model.stash.projects.UserProjects;
import com.noiseapps.itassistant.model.stash.pullrequests.MergeStatus;
import com.noiseapps.itassistant.model.stash.pullrequests.PullRequest;
import com.noiseapps.itassistant.model.stash.pullrequests.details.DetailsBase;
import com.noiseapps.itassistant.utils.annotations.DELETEBODY;

import org.androidannotations.annotations.Bean;

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

    @GET("/rest/api/1.0/projects/{projectKey}/repos/{repoSlug}/commits")
    Observable<PagedApiModel<Commit>> getCommits(@Path("projectKey") String projectKey,
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

    @GET("/rest/api/1.0/projects/{projectKey}/repos/{repoSlug}/pull-requests?state=all")
    PagedApiModel<PullRequest> getPullRequests(@Path("projectKey") String projectKey,
                                               @Path("repoSlug") String repoSlug,
                                               @Query("start") int start);

    @POST("/rest/api/1.0/projects/{projectKey}/repos/{repoSlug}/pull-requests")
    Observable<PullRequest> addPullRequest(@Path("projectKey") String projectKey,
                                        @Path("repoSlug") String repoSlug,
                                        @Body PullRequest pullRequest);

    @GET("/rest/api/1.0/projects/{projectKey}/repos/{repoSlug}/pull-requests/{prId}/merge")
    Observable<MergeStatus> checkPullRequestStatus(@Path("projectKey") String projectKey,
                                                   @Path("repoSlug") String repoSlug,
                                                   @Path("prId") int pullRequestId);

    @POST("/rest/api/1.0/projects/{projectKey}/repos/{repoSlug}/pull-requests/{prId}/merge")
    Observable<PullRequest> mergePullRequest(@Path("projectKey") String projectKey,
                                             @Path("repoSlug") String repoSlug,
                                             @Path("prId") int pullRequestId,
                                             @Query("version") long pullRequestVersion);

    @POST("/rest/api/1.0/projects/{projectKey}/repos/{repoSlug}/pull-requests/{prId}/approve")
    Observable<PullRequest> approvePullRequest(@Path("projectKey") String projectKey,
                                               @Path("repoSlug") String repoSlug,
                                               @Path("prId") int pullRequestId,
                                               @Query("version") long pullRequestVersion);

    @DELETE("/rest/api/1.0/projects/{projectKey}/repos/{repoSlug}/pull-requests/{prId}/approve")
    Observable<PullRequest> unApprovePullRequest(@Path("projectKey") String projectKey,
                                                 @Path("repoSlug") String repoSlug,
                                                 @Path("prId") int pullRequestId,
                                                 @Query("version") long pullRequestVersion);

    @POST("/rest/api/1.0/projects/{projectKey}/repos/{repoSlug}/pull-requests/{prId}/decline")
    Observable<PullRequest> declinePullRequest(@Path("projectKey") String projectKey,
                                               @Path("repoSlug") String repoSlug,
                                               @Path("prId") int pullRequestId,
                                               @Query("version") long pullRequestVersion);

    @POST("/rest/api/1.0/projects/{projectKey}/repos/{repoSlug}/pull-requests/{prId}/reopen")
    Observable<PullRequest> reopenPullRequest(@Path("projectKey") String projectKey,
                                              @Path("repoSlug") String repoSlug,
                                              @Path("prId") int pullRequestId,
                                              @Query("version") long pullRequestVersion);

    @GET("/rest/api/1.0/users")
    PagedApiModel<StashUser> getUserList(@Query("start") int start);


    @GET("/rest/api/1.0/projects/{projectKey}/repos/{repoSlug}/compare/diff")
    Observable<DetailsBase> getPullRequestDiff(@Path("projectKey") String projectKey,
                                               @Path("repoSlug") String repoSlug,
                                               @Query("from") String from,
                                               @Query("to") String to);


}
