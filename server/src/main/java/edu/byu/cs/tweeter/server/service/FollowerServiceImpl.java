package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.FollowerService_I;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.server.dao.FollowingDAO;

public class FollowerServiceImpl implements FollowerService_I {
    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link FollowingDAO} to
     * get the followees.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    @Override
    public FollowerResponse getFollowers(FollowerRequest request) {
        return getFollowerDAO().getFollowers(request);
    }

    /**
     * Returns an instance of {@link FollowingDAO}. Allows mocking of the FollowingDAO class
     * for testing purposes. All usages of FollowingDAO should get their FollowingDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    FollowingDAO getFollowerDAO() {
        return new FollowingDAO();
    }

}
