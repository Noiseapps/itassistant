package com.noiseapps.itassistant.model.stash.projects;

import com.google.gson.annotations.Expose;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class Links {

    @Expose
    private List<Self> self = new ArrayList<Self>();

    /**
     * @return The self
     */
    public List<Self> getSelf() {
        return self;
    }

    /**
     * @param self The self
     */
    public void setSelf(List<Self> self) {
        this.self = self;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(self).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Links)) {
            return false;
        }
        Links rhs = ((Links) other);
        return new EqualsBuilder().append(self, rhs.self).isEquals();
    }

}
