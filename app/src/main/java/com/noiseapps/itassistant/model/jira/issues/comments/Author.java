package com.noiseapps.itassistant.model.jira.issues.comments;

public class Author {
    private String displayName;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Author author = (Author) o;

        return !(displayName != null ? !displayName.equals(author.displayName) : author.displayName != null);

    }

    @Override
    public int hashCode() {
        return displayName != null ? displayName.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Author{" +
                "displayName='" + displayName + '\'' +
                '}';
    }


}
