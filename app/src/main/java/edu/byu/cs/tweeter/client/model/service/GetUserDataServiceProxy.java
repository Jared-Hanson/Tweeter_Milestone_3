package edu.byu.cs.tweeter.client.model.service;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade_For_M3;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.GetUserDataService_I;
import edu.byu.cs.tweeter.model.service.request.GetUserDataRequest;
import edu.byu.cs.tweeter.model.service.response.GetUserDataResponse;
import edu.byu.cs.tweeter.client.util.ByteArrayUtils;

public class GetUserDataServiceProxy implements GetUserDataService_I {

    private static final String URL_PATH = "/getuser";

    public GetUserDataResponse getUserData(GetUserDataRequest getUserDataRequest) throws IOException, TweeterRemoteException {
        ServerFacade_For_M3 serverFacade = getServerFacade();

        GetUserDataResponse getUserDataResponse = serverFacade.getUserFromAlias(getUserDataRequest, URL_PATH);

        if(getUserDataResponse.isSuccess() && getUserDataResponse.getUser().getImageBytes() == null) {
            loadImage(getUserDataResponse.getUser());
        }
        return getUserDataResponse;
    }


    private void loadImage(User user) throws IOException {
        byte [] bytes = ByteArrayUtils.bytesFromUrl(user.getImageUrl());
        user.setImageBytes(bytes);
    }

    /**
     * Returns an instance of {@link ServerFacade}. Allows mocking of the ServerFacade class for
     * testing purposes. All usages of ServerFacade should get their ServerFacade instance from this
     * method to allow for proper mocking.
     *
     * @return the instance.
     *
     */
    public ServerFacade_For_M3 getServerFacade() {
        return new ServerFacade_For_M3();
    }
}
