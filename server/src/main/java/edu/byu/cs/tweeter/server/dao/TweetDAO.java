package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.service.request.TweetRequest;
import edu.byu.cs.tweeter.model.service.response.TweetResponse;

public class TweetDAO {
    public TweetResponse postTweet(TweetRequest request) {
        TweetResponse response = new TweetResponse(true, request.getTweetBody());
        return response;
    }
}
