package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.TweetService_I;
import edu.byu.cs.tweeter.model.service.request.TweetRequest;
import edu.byu.cs.tweeter.model.service.response.TweetResponse;
import edu.byu.cs.tweeter.server.dao.TweetDAO;

public class TweetServiceImpl implements TweetService_I {
    @Override
    public TweetResponse postTweet(TweetRequest request){
        return getTweetDAO().postTweet(request);
    }

    /**
     * Returns an instance of {@link TweetDAO}. Allows mocking of the TweetDAO class
     * for testing purposes. All usages of TweetDAO should get their TweetDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    TweetDAO getTweetDAO() {
        return new TweetDAO();
    }
}
