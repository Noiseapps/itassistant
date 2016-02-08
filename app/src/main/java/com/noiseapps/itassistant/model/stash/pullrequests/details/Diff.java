package com.noiseapps.itassistant.model.stash.pullrequests.details;

import java.util.List;

public class Diff {
    Ref source;
    Ref destination;
    List<Hunk> hunks;
    boolean truncated;

    @Override
    public String toString() {
        return "Diff{" +
                "source=" + source +
                ", destination=" + destination +
                ", hunks=" + hunks +
                ", truncated=" + truncated +
                '}';
    }

    public Ref getSource() {
        return source;
    }

    public Ref getDestination() {
        return destination;
    }

    public List<Hunk> getHunks() {
        return hunks;
    }

    public boolean isTruncated() {
        return truncated;
    }
}
