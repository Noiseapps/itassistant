package com.noiseapps.itassistant.model.stash.pullrequests.details;

public class Line {
    int destination;
    int source;
    String line;
    boolean truncated;

    @Override
    public String toString() {
        return "Line{" +
                "destination=" + destination +
                ", source=" + source +
                ", line='" + line + '\'' +
                ", truncated=" + truncated +
                '}';
    }

    public int getDestination() {
        return destination;
    }

    public int getSource() {
        return source;
    }

    public String getLine() {
        return line;
    }

    public boolean isTruncated() {
        return truncated;
    }
}
