package com.noiseapps.itassistant.model.stash.general;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ProjectRepos implements Parcelable {
    public static final Parcelable.Creator<ProjectRepos> CREATOR = new Parcelable.Creator<ProjectRepos>() {
        public ProjectRepos createFromParcel(Parcel source) {
            return new ProjectRepos(source);
        }

        public ProjectRepos[] newArray(int size) {
            return new ProjectRepos[size];
        }
    };
    private int size;
    @SerializedName("values")
    private List<StashRepoMeta> repos;

    public ProjectRepos() {
    }

    protected ProjectRepos(Parcel in) {
        this.size = in.readInt();
        this.repos = new ArrayList<StashRepoMeta>();
        in.readList(this.repos, List.class.getClassLoader());
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<StashRepoMeta> getRepos() {
        return repos;
    }

    public void setRepos(List<StashRepoMeta> repos) {
        this.repos = repos;
    }

    @Override
    public String toString() {
        return "ProjectRepos{" +
                "size=" + size +
                ", repos=" + repos +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.size);
        dest.writeList(this.repos);
    }
}
