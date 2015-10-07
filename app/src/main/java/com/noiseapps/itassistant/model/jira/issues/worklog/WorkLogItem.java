package com.noiseapps.itassistant.model.jira.issues.worklog;

import com.noiseapps.itassistant.model.jira.issues.common.Author;

public class WorkLogItem {

    private Author author;

    private String comment;

    private String started;

    private String timeSpent;

    private int timeSpentSeconds;

    private int id;

    @Override
    public String toString() {
        return "WorkLogItem{" +
                "author=" + author +
                ", comment='" + comment + '\'' +
                ", started='" + started + '\'' +
                ", timeSpent='" + timeSpent + '\'' +
                ", timeSpentSeconds=" + timeSpentSeconds +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkLogItem that = (WorkLogItem) o;

        if (timeSpentSeconds != that.timeSpentSeconds) return false;
        if (id != that.id) return false;
        if (author != null ? !author.equals(that.author) : that.author != null) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
        if (started != null ? !started.equals(that.started) : that.started != null) return false;
        return !(timeSpent != null ? !timeSpent.equals(that.timeSpent) : that.timeSpent != null);

    }

    @Override
    public int hashCode() {
        int result = author != null ? author.hashCode() : 0;
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (started != null ? started.hashCode() : 0);
        result = 31 * result + (timeSpent != null ? timeSpent.hashCode() : 0);
        result = 31 * result + timeSpentSeconds;
        result = 31 * result + id;
        return result;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStarted() {
        return started;
    }

    public void setStarted(String started) {
        this.started = started;
    }

    public String getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(String timeSpent) {
        this.timeSpent = timeSpent;
    }

    public int getTimeSpentSeconds() {
        return timeSpentSeconds;
    }

    public void setTimeSpentSeconds(int timeSpentSeconds) {
        this.timeSpentSeconds = timeSpentSeconds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
