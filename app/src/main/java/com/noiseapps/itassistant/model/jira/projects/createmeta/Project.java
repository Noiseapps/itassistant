
package com.noiseapps.itassistant.model.jira.projects.createmeta;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Project {

    @SerializedName("expand")
    @Expose
    public String expand;
    @SerializedName("self")
    @Expose
    public String self;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("key")
    @Expose
    public String key;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("avatarUrls")
    @Expose
    public AvatarUrls avatarUrls;
    @SerializedName("issuetypes")
    @Expose
    public List<Issuetype> issuetypes = new ArrayList<Issuetype>();

}
