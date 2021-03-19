package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.FollowDataService_I;
import edu.byu.cs.tweeter.model.service.request.FollowDataRequest;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.response.FollowDataResponse;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.server.dao.FollowerDAO;
import edu.byu.cs.tweeter.server.dao.FollowingDAO;

public class FollowDataServiceImpl implements FollowDataService_I {
    @Override
    public FollowDataResponse getFollowerData(FollowDataRequest request) {
        return new FollowDataResponse(getFollowerDAO().getFollowerCount(request.getUser()), getFollowingDAO().getFolloweeCount(request.getUser()));
    }

    /**
     * Returns an instance of {@link FollowingDAO}. Allows mocking of the FollowingDAO class
     * for testing purposes. All usages of FollowingDAO should get their FollowingDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    FollowerDAO getFollowerDAO() {
        return new FollowerDAO();
    }
    FollowingDAO getFollowingDAO() {
        return new FollowingDAO();
    }
}
