package edu.byu.cs.tweeter.model.service.request;

import java.util.Objects;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class TweetRequest {
    private String user;
    private String tweetBody;
    private Long date;
    private AuthToken authToken;


    public TweetRequest(String user, String tweetBody, Long date, AuthToken authToken) {
        this.user = user;
        this.tweetBody = tweetBody;
        this.date = date;
        this.authToken = authToken;
    }

    public TweetRequest(){
    }

    public String getUser() {
        return user;
    }

    public String getTweetBody() {
        return tweetBody;
    }

    public Long getDate() {
        return date;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setTweetBody(String tweetBody) {
        this.tweetBody = tweetBody;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TweetRequest)) return false;
        TweetRequest that = (TweetRequest) o;
        return Objects.equals(getUser(), that.getUser()) &&
                Objects.equals(getTweetBody(), that.getTweetBody()) &&
                Objects.equals(getDate(), that.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser(), getTweetBody(), getDate());
    }
}
