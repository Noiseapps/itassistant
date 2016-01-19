package com.noiseapps.itassistant.model.stash.projects;

import com.google.gson.annotations.SerializedName;
// suppress, data populated through gson
@SuppressWarnings("unused")
public class BranchModel {
    private String id;
    private String displayId;
    private String latestChangeset;
    private boolean isDefault;
    private Metadata metadata;

    public String getId() {
        return id;
    }

    public String getDisplayId() {
        return displayId;
    }

    public String getLatestChangeset() {
        return latestChangeset;
    }

    public boolean isDefault() {
        return isDefault;
    }

    @Override
    public String toString() {
        return displayId;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public static class Metadata {
        @SerializedName("com.atlassian.stash.stash-branch-utils:ahead-behind-metadata-provider")
        private BehindAheadMetadata behindAheadMetadata;
        @SerializedName("com.atlassian.stash.stash-branch-utils:latest-changeset-metadata")
        private LatestChangesetMetadata changesetMetadata;

        public BehindAheadMetadata getBehindAheadMetadata() {
            return behindAheadMetadata;
        }

        public LatestChangesetMetadata getChangesetMetadata() {
            return changesetMetadata;
        }
    }

    public static class BehindAheadMetadata {
        private int ahead;
        private int behind;

        public int getBehind() {
            return behind;
        }

        public void setBehind(int behind) {
            this.behind = behind;
        }

        public int getAhead() {
            return ahead;
        }

        public void setAhead(int ahead) {
            this.ahead = ahead;
        }
    }

    public static class LatestChangesetMetadata {
        private String id;
        private String displayId;
        private AuthorMetadata author;
        private long authorTimestamp;

        public String getId() {
            return id;
        }

        public String getDisplayId() {
            return displayId;
        }

        public AuthorMetadata getAuthor() {
            return author;
        }

        public long getAuthorTimestamp() {
            return authorTimestamp;
        }
    }

}
