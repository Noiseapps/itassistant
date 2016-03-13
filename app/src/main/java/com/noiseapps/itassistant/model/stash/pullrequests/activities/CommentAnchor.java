package com.noiseapps.itassistant.model.stash.pullrequests.activities;

public class CommentAnchor {

    int line;
    String path;
    String lineType;
    String fileType;

    @Override
    public String toString() {
        return "CommentAnchor{" +
                "line=" + line +
                ", path='" + path + '\'' +
                ", lineType='" + lineType + '\'' +
                ", fileType='" + fileType + '\'' +
                '}';
    }

    public String getLineType() {
        return lineType;
    }

    public String getFileType() {
        return fileType;
    }

    public int getLine() {
        return line;
    }

    public String getPath() {
        return path;
    }
}
