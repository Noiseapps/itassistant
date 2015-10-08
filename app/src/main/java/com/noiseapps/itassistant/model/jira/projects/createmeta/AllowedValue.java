
package com.noiseapps.itassistant.model.jira.projects.createmeta;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllowedValue {

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

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSubtask() {
        return subtask;
    }

    public void setSubtask(boolean subtask) {
        this.subtask = subtask;
    }
}
