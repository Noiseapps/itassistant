package com.noiseapps.itassistant.model.jira.session;

public class SessionRequest {
    private final String username;
    private final String password;

    public SessionRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
