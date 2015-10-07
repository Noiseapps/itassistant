package com.noiseapps.itassistant.model.jira.issues.comments;

import com.noiseapps.itassistant.model.jira.issues.common.Author;

public class Comment {
    private String id;
    private Author author;
    private String body;
    private String created;


    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", author=" + author +
                ", body='" + body + '\'' +
                ", created='" + created + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        if (id != null ? !id.equals(comment.id) : comment.id != null) return false;
        if (author != null ? !author.equals(comment.author) : comment.author != null) return false;
        if (body != null ? !body.equals(comment.body) : comment.body != null) return false;
        return !(created != null ? !created.equals(comment.created) : comment.created != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (created != null ? created.hashCode() : 0);
        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
