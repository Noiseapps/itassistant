package com.noiseapps.itassistant.model.account;

import android.os.Parcel;
import android.os.Parcelable;

public class BaseAccount implements Parcelable {
    public static final Creator<BaseAccount> CREATOR = new Creator<BaseAccount>() {
        public BaseAccount createFromParcel(Parcel source) {
            return new BaseAccount(source);
        }

        public BaseAccount[] newArray(int size) {
            return new BaseAccount[size];
        }
    };
    private final int id;
    private final String username;
    private final String name;
    private final String password;
    private final String url;
    @AccountTypes.AccountType
    private final int accountType;
    private String avatarPath;
    public BaseAccount(int id, String username, String name, String password, String url, String avatarPath, @AccountTypes.AccountType int accountType) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.password = password;
        this.url = url;
        this.avatarPath = avatarPath;
        this.accountType = accountType;
    }
    protected BaseAccount(Parcel in) {
        this.id = in.readInt();
        this.username = in.readString();
        this.name = in.readString();
        this.password = in.readString();
        this.url = in.readString();
        this.avatarPath = in.readString();
        this.accountType = in.readInt();
    }

    @Override
    public String toString() {
        return "BaseAccount{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", url='" + url + '\'' +
                ", avatarPath='" + avatarPath + '\'' +
                ", accountType=" + accountType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseAccount that = (BaseAccount) o;

        if (id != that.id) return false;
        if (accountType != that.accountType) return false;
        if (username != null ? !username.equals(that.username) : that.username != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null)
            return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        return !(avatarPath != null ? !avatarPath.equals(that.avatarPath) : that.avatarPath != null);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (avatarPath != null ? avatarPath.hashCode() : 0);
        result = 31 * result + accountType;
        return result;
    }

    public String getName() {

        return name;
    }

    @AccountTypes.AccountType
    public int getAccountType() {
        return accountType;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUrl() {
        return url;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.username);
        dest.writeString(this.name);
        dest.writeString(this.password);
        dest.writeString(this.url);
        dest.writeString(this.avatarPath);
        dest.writeInt(this.accountType);
    }
}
