package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.service.request.FollowDataRequest;
import edu.byu.cs.tweeter.model.service.response.FollowDataResponse;

public class FollowDataService {

    public FollowDataResponse getFollowerData(FollowDataRequest request) throws IOException {
        FollowDataResponse response = getServerFacade().getFollowerData(request);


        return response;
    }

    public ServerFacade getServerFacade() {
        return new ServerFacade();
    }
}
