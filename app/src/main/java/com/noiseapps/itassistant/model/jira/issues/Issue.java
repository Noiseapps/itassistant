package com.noiseapps.itassistant.model.jira.issues;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Issue {

    @SerializedName("expand")
    @Expose
    private String expand;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("self")
    @Expose
    private String self;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("fields")
    @Expose
    private Fields fields;
    @SerializedName("transitions")
    @Expose
    private List<Transition> transitions;

    public Issue() {
    }

    public Issue(String expand, String id, String self, String key, Fields fields, List<Transition> transitions) {
        this.expand = expand;
        this.id = id;
        this.self = self;
        this.key = key;
        this.fields = fields;
        this.transitions = transitions;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "transitions=" + transitions +
                ", fields=" + fields +
                ", key='" + key + '\'' +
                ", self='" + self + '\'' +
                ", id='" + id + '\'' +
                ", expand='" + expand + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Issue issue = (Issue) o;

        if (expand != null ? !expand.equals(issue.expand) : issue.expand != null) return false;
        if (id != null ? !id.equals(issue.id) : issue.id != null) return false;
        if (self != null ? !self.equals(issue.self) : issue.self != null) return false;
        if (key != null ? !key.equals(issue.key) : issue.key != null) return false;
        if (fields != null ? !fields.equals(issue.fields) : issue.fields != null) return false;
        return !(transitions != null ? !transitions.equals(issue.transitions) : issue.transitions != null);

    }

    @Override
    public int hashCode() {
        int result = expand != null ? expand.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (self != null ? self.hashCode() : 0);
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (fields != null ? fields.hashCode() : 0);
        result = 31 * result + (transitions != null ? transitions.hashCode() : 0);
        return result;
    }

    public String getExpand() {
        return expand;
    }

    public void setExpand(String expand) {
        this.expand = expand;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Fields getFields() {
        return fields;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public void setTransitions(List<Transition> transitions) {
        this.transitions = transitions;
    }
}
