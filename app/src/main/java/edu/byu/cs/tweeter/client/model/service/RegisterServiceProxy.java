package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade_For_M3;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.RegisterService_I;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.client.util.ByteArrayUtils;

/**
 * Contains the business logic to support the register operation.
 */
public class RegisterServiceProxy implements RegisterService_I {

    private static final String URL_PATH = "/register";

    public LoginResponse register(RegisterRequest request) throws IOException, TweeterRemoteException {


        ServerFacade_For_M3 serverFacade = getServerFacade();
        LoginResponse registerResponse = serverFacade.register(request, URL_PATH);

        if(registerResponse.isSuccess() && registerResponse.getUser().getImageBytes() == null) {
            loadImage(registerResponse.getUser());
        }

        return registerResponse;
    }

    /**
     * Loads the profile image data for the user.
     *
     * @param user the user whose profile image data is to be loaded.
     */
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
     */
    public ServerFacade_For_M3 getServerFacade() {
        return new ServerFacade_For_M3();
    }
}
