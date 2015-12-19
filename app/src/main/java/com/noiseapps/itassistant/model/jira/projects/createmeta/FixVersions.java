package com.noiseapps.itassistant.model.jira.projects.createmeta;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class FixVersions {

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
    private List<String> operations = new ArrayList<String>();
    @SerializedName("allowedValues")
    @Expose
    private List<AllowedValue> allowedValues = new ArrayList<>();

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

    public List<String> getOperations() {
        return operations;
    }

    public void setOperations(List<String> operations) {
        this.operations = operations;
    }

    public List<AllowedValue> getAllowedValues() {
        return allowedValues;
    }

    public void setAllowedValues(List<AllowedValue> allowedValues) {
        this.allowedValues = allowedValues;
    }
}
