package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowActionRequest;
import edu.byu.cs.tweeter.model.service.response.FollowActionResponse;

public interface FollowActionService_I {
    public FollowActionResponse followUser(FollowActionRequest request) throws IOException, TweeterRemoteException;

    public FollowActionResponse unFollowUser(FollowActionRequest request) throws IOException, TweeterRemoteException;

    public FollowActionResponse isFollowing(FollowActionRequest request) throws IOException, TweeterRemoteException;
}
