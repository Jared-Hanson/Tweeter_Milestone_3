package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.FollowActionService_I;
import edu.byu.cs.tweeter.model.service.request.FollowActionRequest;
import edu.byu.cs.tweeter.model.service.response.FollowActionResponse;
import edu.byu.cs.tweeter.server.dao.FollowingDAO;

public class FollowActionServiceImpl implements FollowActionService_I {
    @Override
    public FollowActionResponse followUser(FollowActionRequest request) {
        return getDAO().followUser(request);
    }

    @Override
    public FollowActionResponse unFollowUser(FollowActionRequest request){
        return getDAO().unFollowUser(request);
    }

    @Override
    public FollowActionResponse isFollowing(FollowActionRequest request)  {
        return getDAO().isFollowing(request);
    }
    private FollowingDAO getDAO(){
        return new FollowingDAO();
    }
}
