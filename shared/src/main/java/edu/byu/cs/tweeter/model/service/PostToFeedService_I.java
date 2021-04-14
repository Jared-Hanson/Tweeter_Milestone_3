package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.PostToFeedRequest;

public interface PostToFeedService_I {
    public String postTweet(PostToFeedRequest request) throws IOException, TweeterRemoteException;
}
