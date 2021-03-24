package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.GetUserDataRequest;
import edu.byu.cs.tweeter.model.service.response.GetUserDataResponse;

public interface GetUserDataService_I {
    public GetUserDataResponse getUserData(GetUserDataRequest getUserDataRequest) throws IOException, TweeterRemoteException;
}
