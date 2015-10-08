
package com.noiseapps.itassistant.model.jira.projects.createmeta;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Schema {

    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("system")
    @Expose
    public String system;

}
