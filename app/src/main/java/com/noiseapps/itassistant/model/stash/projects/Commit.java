package com.noiseapps.itassistant.model.stash.projects;

public class Commit {
    private String id;
    private String displayId;
    private String message;
    private long authorTimestamp;
    private AuthorMetadata author;

    public String getId() {
        return id;
    }

    public String getDisplayId() {
        return displayId;
    }

    public String getMessage() {
        return message;
    }

    public long getAuthorTimestamp() {
        return authorTimestamp;
    }

    public AuthorMetadata getAuthorMetadata() {
        return author;
    }
}
