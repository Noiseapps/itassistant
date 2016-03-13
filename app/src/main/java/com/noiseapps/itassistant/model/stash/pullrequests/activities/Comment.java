package com.noiseapps.itassistant.model.stash.pullrequests.activities;

import com.noiseapps.itassistant.model.stash.general.StashUser;

public class Comment {
    int id;
    int version;
    String text;
    StashUser author;
    long createdDate;
    long updatedDate;

    // TODO: 26.02.2016 Comment comments;  - todo bo na razie nie będę tego umieszczał i bo nie wiem czy to będzie to :)

    public int getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public String getText() {
        return text;
    }

    public StashUser getAuthor() {
        return author;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public long getUpdatedDate() {
        return updatedDate;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", version=" + version +
                ", text='" + text + '\'' +
                ", author=" + author +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }

    public class PermittedOperations {
        boolean deletable;
        boolean editable;
    }
}


