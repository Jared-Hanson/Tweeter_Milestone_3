package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.TweetService_I;
import edu.byu.cs.tweeter.model.service.request.TweetRequest;
import edu.byu.cs.tweeter.model.service.response.TweetResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.StoryDAO;

public class TweetServiceImpl implements TweetService_I {
    @Override
    public TweetResponse postTweet(TweetRequest request){
        if (!getAuthTokenDAO().checkToken(request.getAuthToken())) {
            return new TweetResponse(false, "Invalid Authorization");
        }
        TweetResponse result =  getStoryDAO().postTweet(request);

        if (result.isSuccess()) {
            //add sqs
        }

        return result;
    }

    /**
     * Returns an instance of {@link StoryDAO}. Allows mocking of the TweetDAO class
     * for testing purposes. All usages of TweetDAO should get their TweetDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    StoryDAO getStoryDAO() {
        return new StoryDAO();
    }

    public AuthTokenDAO getAuthTokenDAO() {
        return new AuthTokenDAO();
    }
}
