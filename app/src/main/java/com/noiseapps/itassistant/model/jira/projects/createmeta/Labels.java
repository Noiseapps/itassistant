
package com.noiseapps.itassistant.model.jira.projects.createmeta;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Labels {

    @SerializedName("required")
    @Expose
    private boolean required;
    @SerializedName("schema")
    @Expose
    private Schema schema;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("autoCompleteUrl")
    @Expose
    private String autoCompleteUrl;
    @SerializedName("operations")
    @Expose
    private List<String> operations = new ArrayList<String>();

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAutoCompleteUrl() {
        return autoCompleteUrl;
    }

    public void setAutoCompleteUrl(String autoCompleteUrl) {
        this.autoCompleteUrl = autoCompleteUrl;
    }

    public List<String> getOperations() {
        return operations;
    }

    public void setOperations(List<String> operations) {
        this.operations = operations;
    }
}
