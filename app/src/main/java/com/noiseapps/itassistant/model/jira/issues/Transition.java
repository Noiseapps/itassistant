package com.noiseapps.itassistant.model.jira.issues;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Transition implements Parcelable {

    public static final Parcelable.Creator<Transition> CREATOR = new Parcelable.Creator<Transition>() {
        public Transition createFromParcel(Parcel source) {
            return new Transition(source);
        }

        public Transition[] newArray(int size) {
            return new Transition[size];
        }
    };
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("to")
    @Expose
    public TransitionTo to;

    public Transition() {
    }

    public Transition(String id, String name, TransitionTo to) {
        this.id = id;
        this.name = name;
        this.to = to;
    }

    protected Transition(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.to = in.readParcelable(TransitionTo.class.getClassLoader());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TransitionTo getTo() {
        return to;
    }

    public void setTo(TransitionTo to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "Transition{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", to=" + to +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transition that = (Transition) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return !(to != null ? !to.equals(that.to) : that.to != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (to != null ? to.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeParcelable(this.to, 0);
    }
}
