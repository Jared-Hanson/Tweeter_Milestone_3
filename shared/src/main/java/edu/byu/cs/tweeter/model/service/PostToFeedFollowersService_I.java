package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.TweetRequest;
import edu.byu.cs.tweeter.model.service.response.TweetResponse;

public interface PostToFeedFollowersService_I {
    public TweetResponse postTweet(TweetRequest request) throws IOException, TweeterRemoteException;
}
