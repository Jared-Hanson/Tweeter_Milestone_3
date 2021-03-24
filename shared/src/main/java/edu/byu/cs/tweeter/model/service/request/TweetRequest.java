package edu.byu.cs.tweeter.model.service.request;

import java.util.Objects;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class TweetRequest {
    private User user;
    private String tweetBody;
    private String date;
    private AuthToken authToken;


    public TweetRequest(User user, String tweetBody, String date, AuthToken authToken) {
        this.user = user;
        this.tweetBody = tweetBody;
        this.date = date;
        this.authToken = authToken;
    }

    public TweetRequest(){
    }

    public User getUser() {
        return user;
    }

    public String getTweetBody() {
        return tweetBody;
    }

    public String getDate() {
        return date;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTweetBody(String tweetBody) {
        this.tweetBody = tweetBody;
    }

    public void setDate(String date) {
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
