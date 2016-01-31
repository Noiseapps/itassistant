package com.noiseapps.itassistant.model.stash.pullrequests;

import android.os.Parcel;
import android.os.Parcelable;

import com.noiseapps.itassistant.model.stash.branches.BranchModel;

import java.util.List;

public class PullRequest implements Parcelable {

    int id;
    String title;
    long updatedDate;
    String state;
    BranchModel fromRef;
    BranchModel toRef;
    PullRequestMember author;
    List<PullRequestMember> reviewers;
    PullRequestAttributes attributes;
    Links links;

    @Override
    public String toString() {
        return "PullRequest{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", updatedDate=" + updatedDate +
                ", state='" + state + '\'' +
                ", fromRef=" + fromRef +
                ", toRef=" + toRef +
                ", author=" + author +
                ", reviewers=" + reviewers +
                ", attributes=" + attributes +
                ", links=" + links +
                '}';
    }

    public Links getLinks() {
        return links;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public long getUpdatedDate() {
        return updatedDate;
    }

    public String getState() {
        return state;
    }

    public BranchModel getFromRef() {
        return fromRef;
    }

    public BranchModel getToRef() {
        return toRef;
    }

    public PullRequestMember getAuthor() {
        return author;
    }

    public List<PullRequestMember> getReviewers() {
        return reviewers;
    }

    public PullRequestAttributes getAttributes() {
        return attributes;
    }

    public PullRequest() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeLong(this.updatedDate);
        dest.writeString(this.state);
        dest.writeParcelable(this.fromRef, 0);
        dest.writeParcelable(this.toRef, 0);
        dest.writeParcelable(this.author, 0);
        dest.writeTypedList(reviewers);
        dest.writeParcelable(this.attributes, 0);
        dest.writeParcelable(this.links, 0);
    }

    protected PullRequest(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.updatedDate = in.readLong();
        this.state = in.readString();
        this.fromRef = in.readParcelable(BranchModel.class.getClassLoader());
        this.toRef = in.readParcelable(BranchModel.class.getClassLoader());
        this.author = in.readParcelable(PullRequestMember.class.getClassLoader());
        this.reviewers = in.createTypedArrayList(PullRequestMember.CREATOR);
        this.attributes = in.readParcelable(PullRequestAttributes.class.getClassLoader());
        this.links = in.readParcelable(Links.class.getClassLoader());
    }

    public static final Creator<PullRequest> CREATOR = new Creator<PullRequest>() {
        public PullRequest createFromParcel(Parcel source) {
            return new PullRequest(source);
        }

        public PullRequest[] newArray(int size) {
            return new PullRequest[size];
        }
    };
}
