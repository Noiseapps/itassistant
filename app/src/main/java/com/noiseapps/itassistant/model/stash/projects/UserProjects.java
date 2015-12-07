
package com.noiseapps.itassistant.model.stash.projects;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class UserProjects {

    @Expose
    private int size;
    @Expose
    private int limit;
    @Expose
    private boolean isLastPage;
    @SerializedName("values")
    private List<StashProject> stashProjects = new ArrayList<>();
    @Expose
    private int start;

    /**
     * 
     * @return
     *     The size
     */
    public int getSize() {
        return size;
    }

    /**
     * 
     * @param size
     *     The size
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * 
     * @return
     *     The limit
     */
    public int getLimit() {
        return limit;
    }

    /**
     * 
     * @param limit
     *     The limit
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * 
     * @return
     *     The isLastPage
     */
    public boolean isIsLastPage() {
        return isLastPage;
    }

    /**
     * 
     * @param isLastPage
     *     The isLastPage
     */
    public void setIsLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
    }

    /**
     * 
     * @return
     *     The stashProjects
     */
    public List<StashProject> getStashProjects() {
        return stashProjects;
    }

    /**
     * 
     * @param stashProjects
     *     The stashProjects
     */
    public void setStashProjects(List<StashProject> stashProjects) {
        this.stashProjects = stashProjects;
    }

    /**
     * 
     * @return
     *     The start
     */
    public int getStart() {
        return start;
    }

    /**
     * 
     * @param start
     *     The start
     */
    public void setStart(int start) {
        this.start = start;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(size).append(limit).append(isLastPage).append(stashProjects).append(start).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UserProjects)) {
            return false;
        }
        UserProjects rhs = ((UserProjects) other);
        return new EqualsBuilder().append(size, rhs.size).append(limit, rhs.limit).append(isLastPage, rhs.isLastPage).append(stashProjects, rhs.stashProjects).append(start, rhs.start).isEquals();
    }

}
