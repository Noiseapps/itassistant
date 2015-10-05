package com.noiseapps.itassistant.model.jira.issues;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Issue implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.expand);
        dest.writeString(this.id);
        dest.writeString(this.self);
        dest.writeString(this.key);
        dest.writeParcelable(this.fields, flags);
        dest.writeTypedList(transitions);
    }

    protected Issue(Parcel in) {
        this.expand = in.readString();
        this.id = in.readString();
        this.self = in.readString();
        this.key = in.readString();
        this.fields = in.readParcelable(Fields.class.getClassLoader());
        this.transitions = in.createTypedArrayList(Transition.CREATOR);
    }

    public static final Parcelable.Creator<Issue> CREATOR = new Parcelable.Creator<Issue>() {
        public Issue createFromParcel(Parcel source) {
            return new Issue(source);
        }

        public Issue[] newArray(int size) {
            return new Issue[size];
        }
    };
}
