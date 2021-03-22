package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.FeedService_I;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.FeedDAO;

public class FeedServiceImpl implements FeedService_I {
    @Override
    public StoryResponse getFeed(StoryRequest request) {
        return getFeedDAO().getFeed(request);
    }

    /**
     * Returns an instance of {@link FeedDAO}. Allows mocking of the FeedDAO class
     * for testing purposes. All usages of FeedDAO should get their FeedDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    FeedDAO getFeedDAO() {
        return new FeedDAO();
    }
}
