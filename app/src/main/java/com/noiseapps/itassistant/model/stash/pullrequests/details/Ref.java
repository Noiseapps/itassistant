package com.noiseapps.itassistant.model.stash.pullrequests.details;

import java.util.List;

public class Ref {
    List<String> components;
    String parent;
    String name;
    String extension;
    String toString;

    public String getExtension() {
        return extension;
    }

    public String getName() {
        return name;
    }

    public String getParent() {
        return parent;
    }

    public List<String> getComponents() {
        return components;
    }

    @Override
    public String toString() {
        return toString;
    }
}
