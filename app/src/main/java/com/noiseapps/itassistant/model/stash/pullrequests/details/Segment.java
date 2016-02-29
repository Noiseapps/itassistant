package com.noiseapps.itassistant.model.stash.pullrequests.details;

import java.util.List;

public class Segment {
    String type;
    List<Line> lines;
    boolean truncated;

    public String getType() {
        return type;
    }

    public List<Line> getLines() {
        return lines;
    }

    public boolean isTruncated() {
        return truncated;
    }

    @Override
    public String toString() {
        return "Segment{" +
                "type='" + type + '\'' +
                ", lines=" + lines +
                ", truncated=" + truncated +
                '}';
    }
}
