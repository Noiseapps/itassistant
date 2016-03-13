package com.noiseapps.itassistant.model.stash.pullrequests.activities;

import com.noiseapps.itassistant.model.stash.general.StashUser;
import com.noiseapps.itassistant.model.stash.pullrequests.details.Diff;

public class PullRequestActivity {
    public static final String ACTION_OPEN = "OPENED";
    public static final String ACTION_UPDATE = "RESCOPED";
    public static final String ACTION_COMMENT = "COMMENTED";
    public static final String ACTION_APPROVED = "APPROVED";
    public static final String ACTION_UNAPPROVED = "UNAPPROVED";
    public static final String ACTION_MERGED = "MERGED";
    public static final String ACTION_DECLINED = "DECLINED";
    public static final String ACTION_REOPENED = "REOPENED";
    int id;
    long createdDate;
    StashUser user;
    String action;
    // only if action is "COMMENTED"
    Comment comment;
    CommentAnchor commentAnchor;
    Diff diff;
    // only used when action is "RESCOPED"
    Changesets added;
    Changesets removed;

    public Changesets getAdded() {
        return added;
    }

    public Changesets getRemoved() {
        return removed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public StashUser getUser() {
        return user;
    }

    public void setUser(StashUser user) {
        this.user = user;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public CommentAnchor getCommentAnchor() {
        return commentAnchor;
    }

    public void setCommentAnchor(CommentAnchor commentAnchor) {
        this.commentAnchor = commentAnchor;
    }

    public Diff getDiff() {
        return diff;
    }

    public void setDiff(Diff diff) {
        this.diff = diff;
    }

    @Override
    public String toString() {
        return "PullRequestActivity{" +
                "id=" + id +
                ", createdDate=" + createdDate +
                ", user=" + user +
                ", action='" + action + '\'' +
                ", comment=" + comment +
                ", commentAnchor=" + commentAnchor +
                ", diff=" + diff +
                ", added=" + added +
                ", removed=" + removed +
                '}';
    }
}
