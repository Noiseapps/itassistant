package com.noiseapps.itassistant.model.jira.session;

public class SessionResponse {

    private final SessionInfo session;
    private final LoginInfo loginInfo;

    public SessionResponse(SessionInfo session, LoginInfo loginInfo) {
        this.session = session;
        this.loginInfo = loginInfo;
    }

    public SessionInfo getSession() {
        return session;
    }

    public LoginInfo getLoginInfo() {
        return loginInfo;
    }

    public final class LoginInfo {
        private final String failedLoginCount;
        private final String loginCount;
        private final String lastFailedLoginTime;
        private final String previousLoginTime;

        public LoginInfo(String failedLoginCount, String loginCount, String lastFailedLoginTime, String previousLoginTime) {
            this.failedLoginCount = failedLoginCount;
            this.loginCount = loginCount;
            this.lastFailedLoginTime = lastFailedLoginTime;
            this.previousLoginTime = previousLoginTime;
        }

        public String getFailedLoginCount() {
            return failedLoginCount;
        }

        public String getLoginCount() {
            return loginCount;
        }

        public String getLastFailedLoginTime() {
            return lastFailedLoginTime;
        }

        public String getPreviousLoginTime() {
            return previousLoginTime;
        }
    }

    public final class SessionInfo {
        private final String name;
        private final String value;

        public SessionInfo(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }

}
