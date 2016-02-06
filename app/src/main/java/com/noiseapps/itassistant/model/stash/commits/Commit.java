package com.noiseapps.itassistant.model.stash.commits;

import com.noiseapps.itassistant.model.stash.general.StashUser;

public class Commit {
    private String id;
    private String displayId;
    private String message;
    private long authorTimestamp;
    private StashUser author;

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

    public StashUser getAuthorMetadata() {
        return author;
    }
}
