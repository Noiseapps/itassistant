package com.noiseapps.itassistant.model.jira.user;

import java.util.Arrays;

public class Groups {

    private final Items[] items;
    private final String size;

    public Groups(Items[] items, String size) {
        this.items = items;
        this.size = size;
    }

    public Items[] getItems() {
        return items;
    }

    public String getSize() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Groups groups = (Groups) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(items, groups.items)) return false;
        return !(size != null ? !size.equals(groups.size) : groups.size != null);

    }

    @Override
    public int hashCode() {
        int result = items != null ? Arrays.hashCode(items) : 0;
        result = 31 * result + (size != null ? size.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Groups{" +
                "items=" + Arrays.toString(items) +
                ", size='" + size + '\'' +
                '}';
    }
}
