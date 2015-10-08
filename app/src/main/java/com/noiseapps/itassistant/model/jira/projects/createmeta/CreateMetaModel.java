
package com.noiseapps.itassistant.model.jira.projects.createmeta;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class CreateMetaModel {

    @SerializedName("expand")
    @Expose
    public String expand;
    @SerializedName("projects")
    @Expose
    public List<Project> projects = new ArrayList<Project>();

}
