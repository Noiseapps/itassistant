package com.noiseapps.itassistant.model.jira.issues;

public class Component {

    private String self;
    private String id;
    private String name;

    @Override
    public String toString() {
        return "Component{" +
                "self='" + self + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Component component = (Component) o;

        if (self != null ? !self.equals(component.self) : component.self != null) return false;
        if (id != null ? !id.equals(component.id) : component.id != null) return false;
        return !(name != null ? !name.equals(component.name) : component.name != null);

    }

    @Override
    public int hashCode() {
        int result = self != null ? self.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
