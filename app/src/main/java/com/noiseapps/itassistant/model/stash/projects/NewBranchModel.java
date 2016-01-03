package com.noiseapps.itassistant.model.stash.projects;

public class NewBranchModel {

    private final String name;
    private final String startPoint;

    public NewBranchModel(String name, String startPoint) {
        this.name = name;
        this.startPoint = startPoint;
    }

    public String getName() {
        return name;
    }

    public String getStartPoint() {
        return startPoint;
    }
}
