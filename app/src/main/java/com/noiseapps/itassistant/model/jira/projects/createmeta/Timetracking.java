
package com.noiseapps.itassistant.model.jira.projects.createmeta;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Timetracking {

    @SerializedName("required")
    @Expose
    public boolean required;
    @SerializedName("schema")
    @Expose
    public Schema schema;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("operations")
    @Expose
    public List<String> operations = new ArrayList<String>();

}
