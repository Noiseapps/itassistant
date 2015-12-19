package com.noiseapps.itassistant.model.jira.issues;

import android.os.Parcel;
import android.os.Parcelable;

public class Component implements Parcelable {

    public static final Parcelable.Creator<Component> CREATOR = new Parcelable.Creator<Component>() {
        public Component createFromParcel(Parcel source) {
            return new Component(source);
        }

        public Component[] newArray(int size) {
            return new Component[size];
        }
    };
    private String self;
    private String id;
    private String name;

    public Component() {
    }

    protected Component(Parcel in) {
        this.self = in.readString();
        this.id = in.readString();
        this.name = in.readString();
    }

    @Override
    public String toString() {
        return "Component{" +
                "self='" + self + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Component component = (Component) o;

        if (self != null ? !self.equals(component.self) : component.self != null) return false;
        if (id != null ? !id.equals(component.id) : component.id != null) return false;
        return !(name != null ? !name.equals(component.name) : component.name != null);

    }

    @Override
    public int hashCode() {
        int result = self != null ? self.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.self);
        dest.writeString(this.id);
        dest.writeString(this.name);
    }
}
