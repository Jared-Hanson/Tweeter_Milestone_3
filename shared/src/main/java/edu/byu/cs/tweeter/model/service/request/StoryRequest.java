package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.Tweet;

public class StoryRequest {
    private String userAlias;
    private int limit;
    private Tweet lastTweet;

    public StoryRequest(String userAlias, int limit, Tweet lastTweet) {
        this.userAlias = userAlias;
        this.limit = limit;
        this.lastTweet = lastTweet;
    }

    public StoryRequest() {}

    public String getUserAlias() {
        return userAlias;
    }

    public int getLimit() {
        return limit;
    }

    public Tweet getLastTweet() {
        return lastTweet;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setLastTweet(Tweet lastTweet) {
        this.lastTweet = lastTweet;
    }
}
