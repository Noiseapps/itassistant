package com.noiseapps.itassistant.model.jira.user;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class Groups implements Parcelable {

    private final Items[] items;
    private final String size;

    public Groups(Items[] items, String size) {
        this.items = items;
        this.size = size;
    }

    public Items[] getItems() {
        return items;
    }

    public String getSize() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Groups groups = (Groups) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(items, groups.items)) return false;
        return !(size != null ? !size.equals(groups.size) : groups.size != null);

    }

    @Override
    public int hashCode() {
        int result = items != null ? Arrays.hashCode(items) : 0;
        result = 31 * result + (size != null ? size.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Groups{" +
                "items=" + Arrays.toString(items) +
                ", size='" + size + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelableArray(this.items, 0);
        dest.writeString(this.size);
    }

    protected Groups(Parcel in) {
        this.items = (Items[]) in.readParcelableArray(Items.class.getClassLoader());
        this.size = in.readString();
    }

    public static final Parcelable.Creator<Groups> CREATOR = new Parcelable.Creator<Groups>() {
        public Groups createFromParcel(Parcel source) {
            return new Groups(source);
        }

        public Groups[] newArray(int size) {
            return new Groups[size];
        }
    };
}
