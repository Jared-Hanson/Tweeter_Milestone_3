package edu.byu.cs.tweeter.model.service.response;

import java.util.List;
import java.util.Objects;

import edu.byu.cs.tweeter.model.domain.Tweet;

public class  StoryResponse extends PagedResponse {

    private List<Tweet> tweets;

    public StoryResponse(String message) {
        super(false, message, false);
    }

    public StoryResponse(List<Tweet> tweets, boolean hasMorePages) {
        super(true, hasMorePages);
        this.tweets = tweets;
    }

    public StoryResponse(boolean success, List<Tweet> tweets, boolean hasMorePages) {
        super(success, hasMorePages);
        this.tweets = tweets;
    }

    public List<Tweet> getTweets() {
        return tweets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StoryResponse)) return false;
        StoryResponse that = (StoryResponse) o;
        return getTweets().equals(that.getTweets());
    }

    public StoryResponse(boolean success) {
        super(success);
        tweets = null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTweets());
    }
}
