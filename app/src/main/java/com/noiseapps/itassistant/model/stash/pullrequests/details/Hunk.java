package com.noiseapps.itassistant.model.stash.pullrequests.details;

import java.util.List;

public class Hunk {

    int sourceLine;
    int sourceSpan;
    int destinationLine;
    int destinationSpan;
    List<Segment> segments;
    boolean truncated;

    @Override
    public String toString() {
        return "Hunk{" +
                "sourceLine=" + sourceLine +
                ", sourceSpan=" + sourceSpan +
                ", destinationLine=" + destinationLine +
                ", destinationSpan=" + destinationSpan +
                ", segments=" + segments +
                ", truncated=" + truncated +
                '}';
    }

    public int getSourceLine() {
        return sourceLine;
    }

    public int getSourceSpan() {
        return sourceSpan;
    }

    public int getDestinationLine() {
        return destinationLine;
    }

    public int getDestinationSpan() {
        return destinationSpan;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public boolean isTruncated() {
        return truncated;
    }
}
