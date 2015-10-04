package com.noiseapps.itassistant.model.jira.user;

public class Items {
    private final String name;

    private final String self;

    public Items(String name, String self) {
        this.name = name;
        this.self = self;
    }

    public String getName() {
        return name;
    }

    public String getSelf() {
        return self;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Items items = (Items) o;

        if (name != null ? !name.equals(items.name) : items.name != null) return false;
        return !(self != null ? !self.equals(items.self) : items.self != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (self != null ? self.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Items{" +
                "name='" + name + '\'' +
                ", self='" + self + '\'' +
                '}';
    }
}
