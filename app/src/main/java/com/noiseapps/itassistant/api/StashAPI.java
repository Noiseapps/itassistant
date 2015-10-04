package com.noiseapps.itassistant.api;

import com.noiseapps.itassistant.model.stash.projects.UserProjects;

import retrofit.Callback;
import retrofit.http.GET;

public interface StashAPI {

    @GET("/rest/api/1.0/projects")
    void getProjects(Callback<UserProjects> projectsCallback);

}
