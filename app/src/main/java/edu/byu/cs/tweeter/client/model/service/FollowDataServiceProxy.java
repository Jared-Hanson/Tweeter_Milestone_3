package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.net.ServerFacade_For_M3;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.FollowDataService_I;
import edu.byu.cs.tweeter.model.service.request.FollowDataRequest;
import edu.byu.cs.tweeter.model.service.response.FollowDataResponse;

public class FollowDataServiceProxy implements FollowDataService_I {
    static final String URL_PATH = "/getfollowdata";

    public FollowDataResponse getFollowerData(FollowDataRequest request) throws IOException, TweeterRemoteException {
        FollowDataResponse response = getServerFacade().getFollowerData(request, URL_PATH);


        return response;
    }

    public ServerFacade_For_M3 getServerFacade() {
        return new ServerFacade_For_M3();
    }
}
