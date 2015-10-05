package com.noiseapps.itassistant.model.account;

import android.os.Parcel;
import android.os.Parcelable;

public class BaseAccount implements Parcelable {

    private final int id;
    private final String username;
    private final String password;
    private final String url;
    private final String avatarPath;
    @AccountTypes.AccountType
    private final int accountType;
    public BaseAccount(int id, String username, String password, String url, String avatarPath, @AccountTypes.AccountType int accountType) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.url = url;
        this.avatarPath = avatarPath;
        this.accountType = accountType;
    }

    @AccountTypes.AccountType
    public int getAccountType(){
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

    @Override
    public String toString() {
        return "BaseAccount{" +
                "id=" + id +
                ", username='" + username + '\'' +
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

        BaseAccount account = (BaseAccount) o;

        if (id != account.id) return false;
        if (accountType != account.accountType) return false;
        if (username != null ? !username.equals(account.username) : account.username != null)
            return false;
        if (password != null ? !password.equals(account.password) : account.password != null)
            return false;
        if (url != null ? !url.equals(account.url) : account.url != null) return false;
        return !(avatarPath != null ? !avatarPath.equals(account.avatarPath) : account.avatarPath != null);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (avatarPath != null ? avatarPath.hashCode() : 0);
        result = 31 * result + accountType;
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeString(this.url);
        dest.writeString(this.avatarPath);
        dest.writeInt(this.accountType);
    }

    protected BaseAccount(Parcel in) {
        this.id = in.readInt();
        this.username = in.readString();
        this.password = in.readString();
        this.url = in.readString();
        this.avatarPath = in.readString();
        this.accountType = in.readInt();
    }

    public static final Parcelable.Creator<BaseAccount> CREATOR = new Parcelable.Creator<BaseAccount>() {
        public BaseAccount createFromParcel(Parcel source) {
            return new BaseAccount(source);
        }

        public BaseAccount[] newArray(int size) {
            return new BaseAccount[size];
        }
    };
}
