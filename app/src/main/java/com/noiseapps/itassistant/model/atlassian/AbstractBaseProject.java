package com.noiseapps.itassistant.model.atlassian;

import android.os.Parcel;
import android.os.Parcelable;

import com.noiseapps.itassistant.model.account.AccountTypes;

public abstract class AbstractBaseProject implements Parcelable {

    @AccountTypes.AccountType
    public abstract int getAccountType();

    private String key;
    private String id;
    private String name;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
    public String toString() {
        return "AbstractBaseProject{" +
                "key='" + key + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractBaseProject that = (AbstractBaseProject) o;

        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return !(name != null ? !name.equals(that.name) : that.name != null);

    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.id);
        dest.writeString(this.name);
    }

    public AbstractBaseProject() {
    }

    protected AbstractBaseProject(Parcel in) {
        this.key = in.readString();
        this.id = in.readString();
        this.name = in.readString();
    }
}
