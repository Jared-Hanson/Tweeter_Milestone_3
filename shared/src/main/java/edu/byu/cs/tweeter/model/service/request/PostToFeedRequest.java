package edu.byu.cs.tweeter.model.service.request;

import java.util.Objects;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class PostToFeedRequest {
    private String follower;
    private String user;
    private String tweetBody;
    private Long date;
    private AuthToken authToken;

    public PostToFeedRequest(String follower, String user, String tweetBody, Long date, AuthToken authToken) {
        this.follower = follower;
        this.user = user;
        this.tweetBody = tweetBody;
        this.date = date;
        this.authToken = authToken;
    }

    public PostToFeedRequest() {}

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTweetBody() {
        return tweetBody;
    }

    public void setTweetBody(String tweetBody) {
        this.tweetBody = tweetBody;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostToFeedRequest)) return false;
        PostToFeedRequest that = (PostToFeedRequest) o;
        return Objects.equals(getUser(), that.getUser()) &&
                Objects.equals(getTweetBody(), that.getTweetBody()) &&
                Objects.equals(getDate(), that.getDate()) &&
                Objects.equals(getFollower(), that.getFollower());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser(), getTweetBody(), getDate(), getFollower());
    }
}
