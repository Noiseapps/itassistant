package com.noiseapps.itassistant.model.jira.user;

import android.os.Parcel;
import android.os.Parcelable;

public class Items implements Parcelable {
    public static final Parcelable.Creator<Items> CREATOR = new Parcelable.Creator<Items>() {
        public Items createFromParcel(Parcel source) {
            return new Items(source);
        }

        public Items[] newArray(int size) {
            return new Items[size];
        }
    };
    private final String name;
    private final String self;

    public Items(String name, String self) {
        this.name = name;
        this.self = self;
    }

    protected Items(Parcel in) {
        this.name = in.readString();
        this.self = in.readString();
    }

    public String getName() {
        return name;
    }

    public String getSelf() {
        return self;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Items items = (Items) o;

        if (name != null ? !name.equals(items.name) : items.name != null) return false;
        return !(self != null ? !self.equals(items.self) : items.self != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (self != null ? self.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Items{" +
                "name='" + name + '\'' +
                ", self='" + self + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.self);
    }
}
