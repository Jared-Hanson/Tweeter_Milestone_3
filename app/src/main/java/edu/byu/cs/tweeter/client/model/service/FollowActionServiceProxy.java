package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.net.ServerFacade_For_M3;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.FollowActionService_I;
import edu.byu.cs.tweeter.model.service.request.FollowActionRequest;
import edu.byu.cs.tweeter.model.service.response.FollowActionResponse;

public class FollowActionServiceProxy implements FollowActionService_I {
    static final String URL_PATHFOLLOW = "/followuser";
    static final String URL_PATHUNFOLLOW = "/unfollowuser";
    static final String URL_PATHISFOLLOW = "/isfollow";

    public FollowActionResponse followUser(FollowActionRequest request) throws IOException, TweeterRemoteException {
        FollowActionResponse res = getServerFacade().followUser(request, URL_PATHFOLLOW);
        return res;
    }
    public FollowActionResponse unFollowUser(FollowActionRequest request) throws IOException, TweeterRemoteException {
        FollowActionResponse res = getServerFacade().unFollowUser(request, URL_PATHUNFOLLOW);
        return res;
    }
    public FollowActionResponse isFollowing(FollowActionRequest request) throws IOException, TweeterRemoteException {
        FollowActionResponse res = getServerFacade().isFollowing(request, URL_PATHISFOLLOW);
        return res;
    }
    public ServerFacade_For_M3 getServerFacade() {
        return new ServerFacade_For_M3();
    }
}
