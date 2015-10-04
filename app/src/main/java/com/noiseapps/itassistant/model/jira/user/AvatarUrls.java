package com.noiseapps.itassistant.model.jira.user;

import com.google.gson.annotations.SerializedName;

public class AvatarUrls
{

    @SerializedName("16x16")
    private final String avatar16;

    @SerializedName("24x24")
    private final String avatar24;

    @SerializedName("32x32")
    private final String avatar32;

    @SerializedName("48x48")
    private final String avatar48;

    public AvatarUrls(String avatar16, String avatar24, String avatar32, String avatar48) {
        this.avatar16 = avatar16;
        this.avatar24 = avatar24;
        this.avatar32 = avatar32;
        this.avatar48 = avatar48;
    }

    public String getAvatar48() {
        return avatar48;
    }

    public String getAvatar32() {
        return avatar32;
    }

    public String getAvatar24() {
        return avatar24;
    }

    public String getAvatar16() {
        return avatar16;
    }

    @Override
    public String toString() {
        return "AvatarUrls{" +
                "avatar16='" + avatar16 + '\'' +
                ", avatar24='" + avatar24 + '\'' +
                ", avatar32='" + avatar32 + '\'' +
                ", avatar48='" + avatar48 + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AvatarUrls that = (AvatarUrls) o;

        if (avatar16 != null ? !avatar16.equals(that.avatar16) : that.avatar16 != null)
            return false;
        if (avatar24 != null ? !avatar24.equals(that.avatar24) : that.avatar24 != null)
            return false;
        if (avatar32 != null ? !avatar32.equals(that.avatar32) : that.avatar32 != null)
            return false;
        return !(avatar48 != null ? !avatar48.equals(that.avatar48) : that.avatar48 != null);

    }

    @Override
    public int hashCode() {
        int result = avatar16 != null ? avatar16.hashCode() : 0;
        result = 31 * result + (avatar24 != null ? avatar24.hashCode() : 0);
        result = 31 * result + (avatar32 != null ? avatar32.hashCode() : 0);
        result = 31 * result + (avatar48 != null ? avatar48.hashCode() : 0);
        return result;
    }
}
