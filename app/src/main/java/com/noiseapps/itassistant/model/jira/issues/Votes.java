
package com.noiseapps.itassistant.model.jira.issues;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


public class Votes {

    @SerializedName("self")
    @Expose
    private String self;
    @SerializedName("votes")
    @Expose
    private long votes;
    @SerializedName("hasVoted")
    @Expose
    private boolean hasVoted;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Votes() {
    }

    /**
     * 
     * @param hasVoted
     * @param votes
     * @param self
     */
    public Votes(String self, long votes, boolean hasVoted) {
        this.self = self;
        this.votes = votes;
        this.hasVoted = hasVoted;
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
     *     The votes
     */
    public long getVotes() {
        return votes;
    }

    /**
     * 
     * @param votes
     *     The votes
     */
    public void setVotes(long votes) {
        this.votes = votes;
    }

    /**
     * 
     * @return
     *     The hasVoted
     */
    public boolean isHasVoted() {
        return hasVoted;
    }

    /**
     * 
     * @param hasVoted
     *     The hasVoted
     */
    public void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(self).append(votes).append(hasVoted).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Votes) == false) {
            return false;
        }
        Votes rhs = ((Votes) other);
        return new EqualsBuilder().append(self, rhs.self).append(votes, rhs.votes).append(hasVoted, rhs.hasVoted).isEquals();
    }

}
