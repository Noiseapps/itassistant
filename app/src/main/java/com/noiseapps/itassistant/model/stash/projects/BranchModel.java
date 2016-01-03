package com.noiseapps.itassistant.model.stash.projects;

public class BranchModel {
    private String id;
    private String displayId;
    private String latestChangeset;
    private String latestCommit;
    private boolean isDefault;


    public String getId() {
        return id;
    }

    public String getDisplayId() {
        return displayId;
    }

    public String getLatestChangeset() {
        return latestChangeset;
    }

    public String getLatestCommit() {
        return latestCommit;
    }

    public boolean isDefault() {
        return isDefault;
    }

    @Override
    public String toString() {
        return displayId;
    }
}
