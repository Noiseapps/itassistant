
package com.noiseapps.itassistant.model.jira.issues;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


public class Watches {

    @SerializedName("self")
    @Expose
    private String self;
    @SerializedName("watchCount")
    @Expose
    private long watchCount;
    @SerializedName("isWatching")
    @Expose
    private boolean isWatching;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Watches() {
    }

    /**
     * 
     * @param watchCount
     * @param isWatching
     * @param self
     */
    public Watches(String self, long watchCount, boolean isWatching) {
        this.self = self;
        this.watchCount = watchCount;
        this.isWatching = isWatching;
    }

    /**
     * 
     * @return
     *     The self
     */
    public String getSelf() {
        return self;
    }

    /**
     * 
     * @param self
     *     The self
     */
    public void setSelf(String self) {
        this.self = self;
    }

    /**
     * 
     * @return
     *     The watchCount
     */
    public long getWatchCount() {
        return watchCount;
    }

    /**
     * 
     * @param watchCount
     *     The watchCount
     */
    public void setWatchCount(long watchCount) {
        this.watchCount = watchCount;
    }

    /**
     * 
     * @return
     *     The isWatching
     */
    public boolean isIsWatching() {
        return isWatching;
    }

    /**
     * 
     * @param isWatching
     *     The isWatching
     */
    public void setIsWatching(boolean isWatching) {
        this.isWatching = isWatching;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(self).append(watchCount).append(isWatching).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Watches) == false) {
            return false;
        }
        Watches rhs = ((Watches) other);
        return new EqualsBuilder().append(self, rhs.self).append(watchCount, rhs.watchCount).append(isWatching, rhs.isWatching).isEquals();
    }

}
