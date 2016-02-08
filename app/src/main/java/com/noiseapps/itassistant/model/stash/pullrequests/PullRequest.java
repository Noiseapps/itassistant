package com.noiseapps.itassistant.model.stash.pullrequests;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.StringDef;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.google.gson.Gson;
import com.noiseapps.itassistant.model.stash.branches.BranchModel;
import com.noiseapps.itassistant.model.stash.general.StashUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class PullRequest implements Parcelable {
    public static final String STATUS_MERGED = "MERGED";
    public static final String STATUS_DECLINED = "DECLINED";
    public static final String STATUS_OPEN = "OPEN";
    public static final Creator<PullRequest> CREATOR = new Creator<PullRequest>() {
        public PullRequest createFromParcel(Parcel source) {
            return new PullRequest(source);
        }

        public PullRequest[] newArray(int size) {
            return new PullRequest[size];
        }
    };
    int id;
    String title;
    long version;
    long updatedDate;
    @PrStatus
    String state;
    BranchModel fromRef;
    BranchModel toRef;
    PullRequestMember author;
    List<PullRequestMember> reviewers;
    PullRequestAttributes attributes;
    Links links;
    public PullRequest() {
    }
    protected PullRequest(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.updatedDate = in.readLong();
        //noinspection WrongConstant
        this.state = in.readString();
        this.fromRef = in.readParcelable(BranchModel.class.getClassLoader());
        this.toRef = in.readParcelable(BranchModel.class.getClassLoader());
        this.author = in.readParcelable(PullRequestMember.class.getClassLoader());
        this.reviewers = in.createTypedArrayList(PullRequestMember.CREATOR);
        this.attributes = in.readParcelable(PullRequestAttributes.class.getClassLoader());
        this.links = in.readParcelable(Links.class.getClassLoader());
    }

    public static PullRequest initialize(String title, BranchModel fromRef, BranchModel toRef, List<StashUser> reviewers) {
        final PullRequest pullRequest = new PullRequest();
        pullRequest.title = title;
        pullRequest.fromRef = fromRef;
        pullRequest.toRef = toRef;
        pullRequest.state = STATUS_OPEN;
        final List<PullRequestMember> members = Stream.of(reviewers).map(stashUser -> {
            final PullRequestMember pullRequestMember = new PullRequestMember();
            final StashUser author = new StashUser();
            author.setName(stashUser.getName());
            pullRequestMember.setUser(author);
            return pullRequestMember;
        }).collect(Collectors.toList());
        pullRequest.reviewers = new ArrayList<>(members);
        return pullRequest;
    }

    public static PullRequest spoof() {
        return new Gson().fromJson("{\"id\":279,\"version\":0,\"title\":\"Testy ITAssistant [DO NOT MERGE]\",\"state\":\"OPEN\",\"open\":true,\"closed\":false,\"createdDate\":1454711351063,\"updatedDate\":1454711351063,\"fromRef\":{\"id\":\"refs/heads/feature/MREG-395\",\"displayId\":\"feature/MREG-395\",\"latestChangeset\":\"b7261b95468e75a347e56e3f115387a66a5bd628\",\"repository\":{\"slug\":\"android\",\"id\":75,\"name\":\"android\",\"scmId\":\"git\",\"state\":\"AVAILABLE\",\"statusMessage\":\"Available\",\"forkable\":true,\"project\":{\"key\":\"MREG\",\"id\":70,\"name\":\"mRegioKonduktor\",\"public\":false,\"type\":\"NORMAL\",\"link\":{\"url\":\"/projects/MREG\",\"rel\":\"self\"},\"links\":{\"self\":[{\"href\":\"http://jira.exaco.pl:7990/projects/MREG\"}]}},\"public\":false,\"link\":{\"url\":\"/projects/MREG/repos/android/browse\",\"rel\":\"self\"},\"cloneUrl\":\"http://tomasz.scibiorek@jira.exaco.pl:7990/scm/mreg/android.git\",\"links\":{\"clone\":[{\"href\":\"ssh://git@jira.exaco.pl:7999/mreg/android.git\",\"name\":\"ssh\"},{\"href\":\"http://tomasz.scibiorek@jira.exaco.pl:7990/scm/mreg/android.git\",\"name\":\"http\"}],\"self\":[{\"href\":\"http://jira.exaco.pl:7990/projects/MREG/repos/android/browse\"}]}}},\"toRef\":{\"id\":\"refs/heads/master\",\"displayId\":\"master\",\"latestChangeset\":\"13e26709396ca246a9f18a5f27f5020becd906dc\",\"repository\":{\"slug\":\"android\",\"id\":75,\"name\":\"android\",\"scmId\":\"git\",\"state\":\"AVAILABLE\",\"statusMessage\":\"Available\",\"forkable\":true,\"project\":{\"key\":\"MREG\",\"id\":70,\"name\":\"mRegioKonduktor\",\"public\":false,\"type\":\"NORMAL\",\"link\":{\"url\":\"/projects/MREG\",\"rel\":\"self\"},\"links\":{\"self\":[{\"href\":\"http://jira.exaco.pl:7990/projects/MREG\"}]}},\"public\":false,\"link\":{\"url\":\"/projects/MREG/repos/android/browse\",\"rel\":\"self\"},\"cloneUrl\":\"http://tomasz.scibiorek@jira.exaco.pl:7990/scm/mreg/android.git\",\"links\":{\"clone\":[{\"href\":\"ssh://git@jira.exaco.pl:7999/mreg/android.git\",\"name\":\"ssh\"},{\"href\":\"http://tomasz.scibiorek@jira.exaco.pl:7990/scm/mreg/android.git\",\"name\":\"http\"}],\"self\":[{\"href\":\"http://jira.exaco.pl:7990/projects/MREG/repos/android/browse\"}]}}},\"locked\":false,\"author\":{\"user\":{\"name\":\"tomasz.scibiorek\",\"emailAddress\":\"tomasz.scibiorek@exaco.pl\",\"id\":29,\"displayName\":\"Tomasz Ścibiorek\",\"active\":true,\"slug\":\"tomasz.scibiorek\",\"type\":\"NORMAL\",\"link\":{\"url\":\"/users/tomasz.scibiorek\",\"rel\":\"self\"},\"links\":{\"self\":[{\"href\":\"http://jira.exaco.pl:7990/users/tomasz.scibiorek\"}]}},\"role\":\"AUTHOR\",\"approved\":false},\"reviewers\":[{\"user\":{\"name\":\"przemeka\",\"emailAddress\":\"przemyslaw.andrzejewski@exaco.pl\",\"id\":13,\"displayName\":\"Przemek Andrzejewski\",\"active\":true,\"slug\":\"przemeka\",\"type\":\"NORMAL\",\"link\":{\"url\":\"/users/przemeka\",\"rel\":\"self\"},\"links\":{\"self\":[{\"href\":\"http://jira.exaco.pl:7990/users/przemeka\"}]}},\"role\":\"REVIEWER\",\"approved\":false}],\"participants\":[],\"link\":{\"url\":\"/projects/MREG/repos/android/pull-requests/279\",\"rel\":\"self\"},\"links\":{\"self\":[{\"href\":\"http://jira.exaco.pl:7990/projects/MREG/repos/android/pull-requests/279\"}]}}", PullRequest.class);
    }

    public long getVersion() {
        return version;
    }

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

    @PrStatus
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

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({STATUS_DECLINED, STATUS_MERGED, STATUS_OPEN})
    public @interface PrStatus {
    }
}
