package edu.byu.cs.tweeter.server.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.PostToFeedService_I;
import edu.byu.cs.tweeter.model.service.request.PostToFeedRequest;
import edu.byu.cs.tweeter.server.dao.FeedDAO;

public class PostToFeedServiceImpl implements PostToFeedService_I {
    @Override
    public String postTweet(PostToFeedRequest request) throws IOException, TweeterRemoteException {
        getFeedDAO().postTweet(request);
        return null;
    }

    FeedDAO getFeedDAO() {
        return new FeedDAO();
    }
}
