package com.noiseapps.itassistant.model.stash.projects;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.noiseapps.itassistant.model.account.AccountTypes;
import com.noiseapps.itassistant.model.atlassian.AbstractBaseProject;

public class StashProject extends AbstractBaseProject {

    public static final Creator<StashProject> CREATOR = new Creator<StashProject>() {
        public StashProject createFromParcel(Parcel source) {
            return new StashProject(source);
        }

        public StashProject[] newArray(int size) {
            return new StashProject[size];
        }
    };
    @SerializedName("public")
    private boolean _public;
    private String type;
    private String description;

    public StashProject() {
    }

    protected StashProject(Parcel in) {
        super(in);
        this._public = in.readByte() != 0;
        this.type = in.readString();
        this.description = in.readString();
    }

    public boolean isPublic() {
        return _public;
    }

    public void setPublic(boolean _public) {
        this._public = _public;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int getAccountType() {
        return AccountTypes.ACC_STASH;
    }

    @Override
    public String toString() {
        return "StashProject{" +
                "_public=" + _public +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        StashProject that = (StashProject) o;

        if (_public != that._public) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        return !(description != null ? !description.equals(that.description) : that.description != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (_public ? 1 : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte(_public ? (byte) 1 : (byte) 0);
        dest.writeString(this.type);
        dest.writeString(this.description);
    }
}
