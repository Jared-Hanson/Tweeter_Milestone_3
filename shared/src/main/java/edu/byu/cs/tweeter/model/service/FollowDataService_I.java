package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowDataRequest;
import edu.byu.cs.tweeter.model.service.response.FollowDataResponse;

public interface FollowDataService_I {
    public FollowDataResponse getFollowerData(FollowDataRequest request) throws IOException, TweeterRemoteException;
}
