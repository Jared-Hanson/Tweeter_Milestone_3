package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.StoryService_I;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.StoryDAO;

public class StoryServiceImpl implements StoryService_I {
    @Override
    public StoryResponse getStory(StoryRequest request) {
        return getStoryDAO().getStory(request);
    }

    /**
     * Returns an instance of {@link StoryDAO}. Allows mocking of the StoryDAO class
     * for testing purposes. All usages of StoryDAO should get their StoryDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    StoryDAO getStoryDAO() {
        return new StoryDAO();
    }
}
