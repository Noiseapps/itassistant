package com.noiseapps.itassistant.api;

import com.noiseapps.itassistant.model.stash.projects.UserProjects;

import retrofit.http.GET;
import rx.Observable;

public interface StashAPI {

    @GET("/rest/api/1.0/projects")
    Observable<UserProjects> getProjects();

}
