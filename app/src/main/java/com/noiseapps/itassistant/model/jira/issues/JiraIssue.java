
package com.noiseapps.itassistant.model.jira.issues;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;


public class JiraIssue {

    @SerializedName("expand")
    @Expose
    private String expand;
    @SerializedName("startAt")
    @Expose
    private long startAt;
    @SerializedName("maxResults")
    @Expose
    private long maxResults;
    @SerializedName("total")
    @Expose
    private long total;
    @SerializedName("issues")
    @Expose
    private List<Issue> issues = new ArrayList<Issue>();

    public JiraIssue() {
    }

    public JiraIssue(String expand, long startAt, long maxResults, long total, List<Issue> issues) {
        this.expand = expand;
        this.startAt = startAt;
        this.maxResults = maxResults;
        this.total = total;
        this.issues = issues;
    }

    /**
     * 
     * @return
     *     The expand
     */
    public String getExpand() {
        return expand;
    }

    /**
     * 
     * @param expand
     *     The expand
     */
    public void setExpand(String expand) {
        this.expand = expand;
    }

    /**
     * 
     * @return
     *     The startAt
     */
    public long getStartAt() {
        return startAt;
    }

    /**
     * 
     * @param startAt
     *     The startAt
     */
    public void setStartAt(long startAt) {
        this.startAt = startAt;
    }

    /**
     * 
     * @return
     *     The maxResults
     */
    public long getMaxResults() {
        return maxResults;
    }

    /**
     * 
     * @param maxResults
     *     The maxResults
     */
    public void setMaxResults(long maxResults) {
        this.maxResults = maxResults;
    }

    /**
     * 
     * @return
     *     The total
     */
    public long getTotal() {
        return total;
    }

    /**
     * 
     * @param total
     *     The total
     */
    public void setTotal(long total) {
        this.total = total;
    }

    /**
     * 
     * @return
     *     The issues
     */
    public List<Issue> getIssues() {
        return issues;
    }

    /**
     * 
     * @param issues
     *     The issues
     */
    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(expand).append(startAt).append(maxResults).append(total).append(issues).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof JiraIssue) == false) {
            return false;
        }
        JiraIssue rhs = ((JiraIssue) other);
        return new EqualsBuilder().append(expand, rhs.expand).append(startAt, rhs.startAt).append(maxResults, rhs.maxResults).append(total, rhs.total).append(issues, rhs.issues).isEquals();
    }

}
