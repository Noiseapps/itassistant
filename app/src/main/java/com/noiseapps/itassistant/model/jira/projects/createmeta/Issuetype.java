
package com.noiseapps.itassistant.model.jira.projects.createmeta;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Issuetype {

    @SerializedName("self")
    @Expose
    public String self;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("iconUrl")
    @Expose
    public String iconUrl;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("subtask")
    @Expose
    public boolean subtask;
    @SerializedName("expand")
    @Expose
    public String expand;
    @SerializedName("fields")
    @Expose
    public Fields fields;

}
