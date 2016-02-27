package com.noiseapps.itassistant.model.stash.commits;

import com.noiseapps.itassistant.model.stash.general.StashUser;
import com.noiseapps.itassistant.model.stash.pullrequests.activities.Attributes;

import org.w3c.dom.Attr;

public class Commit {
    private String id;
    private String displayId;
    private String message;
    private long authorTimestamp;
    private StashUser author;

    public Attributes getAttributes() {
        return attributes;
    }

    private Attributes attributes;

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
