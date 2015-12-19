package com.noiseapps.itassistant.model.jira.projects.createmeta;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Attachment {

    @SerializedName("required")
    @Expose
    private boolean required;
    @SerializedName("schema")
    @Expose
    private Schema schema;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("operations")
    @Expose
    private List<Object> operations = new ArrayList<Object>();

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

    public List<Object> getOperations() {
        return operations;
    }

    public void setOperations(List<Object> operations) {
        this.operations = operations;
    }
}
