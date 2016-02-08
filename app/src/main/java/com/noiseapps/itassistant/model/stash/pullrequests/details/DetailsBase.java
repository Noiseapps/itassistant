package com.noiseapps.itassistant.model.stash.pullrequests.details;

import java.util.List;

public class DetailsBase {
    String fromHash;
    String toHash;
    int contextLines;
    String whitespace;
    List<Diff> diffs;

    public String getFromHash() {
        return fromHash;
    }

    public String getToHash() {
        return toHash;
    }

    public int getContextLines() {
        return contextLines;
    }

    public String getWhitespace() {
        return whitespace;
    }

    public List<Diff> getDiffs() {
        return diffs;
    }

    @Override
    public String toString() {
        return "DetailsBase{" +
                "fromHash='" + fromHash + '\'' +
                ", toHash='" + toHash + '\'' +
                ", contextLines=" + contextLines +
                ", whitespace='" + whitespace + '\'' +
                ", diffs=" + diffs +
                '}';
    }
}
