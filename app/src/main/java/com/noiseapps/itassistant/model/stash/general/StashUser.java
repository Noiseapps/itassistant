package com.noiseapps.itassistant.model.stash.general;

import com.noiseapps.itassistant.model.stash.pullrequests.Links;


public class StashUser {
    int id;
    String name;
    String displayName;
    String emailAddress;
    String slug;
    Links links;

    public Links getLinks() {
        return links;
    }

    @Override
    public String toString() {
        return "StashUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", slug='" + slug + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getSlug() {
        return slug;
    }
}
