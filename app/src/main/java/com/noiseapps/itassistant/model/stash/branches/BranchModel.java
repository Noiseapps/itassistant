package com.noiseapps.itassistant.model.stash.branches;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.noiseapps.itassistant.model.stash.commits.AuthorMetadata;

// suppress, data populated through gson
@SuppressWarnings("unused")
public class BranchModel implements Parcelable {
    private String id;
    private String displayId;
    private String latestChangeset;
    private boolean isDefault;
    private Metadata metadata;

    public String getId() {
        return id;
    }

    public String getDisplayId() {
        return displayId;
    }

    public String getLatestChangeset() {
        return latestChangeset;
    }

    public boolean isDefault() {
        return isDefault;
    }

    @Override
    public String toString() {
        return displayId;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public static class Metadata implements Parcelable {
        @SerializedName("com.atlassian.stash.stash-branch-utils:ahead-behind-metadata-provider")
        private BehindAheadMetadata behindAheadMetadata;
        @SerializedName("com.atlassian.stash.stash-branch-utils:latest-changeset-metadata")
        private LatestChangesetMetadata changesetMetadata;

        public BehindAheadMetadata getBehindAheadMetadata() {
            return behindAheadMetadata;
        }

        public LatestChangesetMetadata getChangesetMetadata() {
            return changesetMetadata;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.behindAheadMetadata, 0);
            dest.writeParcelable(this.changesetMetadata, 0);
        }

        public Metadata() {
        }

        protected Metadata(Parcel in) {
            this.behindAheadMetadata = in.readParcelable(BehindAheadMetadata.class.getClassLoader());
            this.changesetMetadata = in.readParcelable(LatestChangesetMetadata.class.getClassLoader());
        }

        public static final Parcelable.Creator<Metadata> CREATOR = new Parcelable.Creator<Metadata>() {
            public Metadata createFromParcel(Parcel source) {
                return new Metadata(source);
            }

            public Metadata[] newArray(int size) {
                return new Metadata[size];
            }
        };
    }

    public static class BehindAheadMetadata implements Parcelable {
        private int ahead;
        private int behind;

        public int getBehind() {
            return behind;
        }

        public void setBehind(int behind) {
            this.behind = behind;
        }

        public int getAhead() {
            return ahead;
        }

        public void setAhead(int ahead) {
            this.ahead = ahead;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.ahead);
            dest.writeInt(this.behind);
        }

        public BehindAheadMetadata() {
        }

        protected BehindAheadMetadata(Parcel in) {
            this.ahead = in.readInt();
            this.behind = in.readInt();
        }

        public static final Parcelable.Creator<BehindAheadMetadata> CREATOR = new Parcelable.Creator<BehindAheadMetadata>() {
            public BehindAheadMetadata createFromParcel(Parcel source) {
                return new BehindAheadMetadata(source);
            }

            public BehindAheadMetadata[] newArray(int size) {
                return new BehindAheadMetadata[size];
            }
        };
    }

    public static class LatestChangesetMetadata implements Parcelable {
        private String id;
        private String displayId;
        private AuthorMetadata author;
        private long authorTimestamp;

        public String getId() {
            return id;
        }

        public String getDisplayId() {
            return displayId;
        }

        public AuthorMetadata getAuthor() {
            return author;
        }

        public long getAuthorTimestamp() {
            return authorTimestamp;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.displayId);
            dest.writeParcelable(this.author, 0);
            dest.writeLong(this.authorTimestamp);
        }

        public LatestChangesetMetadata() {
        }

        protected LatestChangesetMetadata(Parcel in) {
            this.id = in.readString();
            this.displayId = in.readString();
            this.author = in.readParcelable(AuthorMetadata.class.getClassLoader());
            this.authorTimestamp = in.readLong();
        }

        public static final Parcelable.Creator<LatestChangesetMetadata> CREATOR = new Parcelable.Creator<LatestChangesetMetadata>() {
            public LatestChangesetMetadata createFromParcel(Parcel source) {
                return new LatestChangesetMetadata(source);
            }

            public LatestChangesetMetadata[] newArray(int size) {
                return new LatestChangesetMetadata[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.displayId);
        dest.writeString(this.latestChangeset);
        dest.writeByte(isDefault ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.metadata, 0);
    }

    public BranchModel() {
    }

    protected BranchModel(Parcel in) {
        this.id = in.readString();
        this.displayId = in.readString();
        this.latestChangeset = in.readString();
        this.isDefault = in.readByte() != 0;
        this.metadata = in.readParcelable(Metadata.class.getClassLoader());
    }

    public static final Parcelable.Creator<BranchModel> CREATOR = new Parcelable.Creator<BranchModel>() {
        public BranchModel createFromParcel(Parcel source) {
            return new BranchModel(source);
        }

        public BranchModel[] newArray(int size) {
            return new BranchModel[size];
        }
    };
}
