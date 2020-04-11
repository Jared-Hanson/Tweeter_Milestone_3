package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.client.util.ByteArrayUtils;
import edu.byu.cs.tweeter.model.service.LoginService;

/**
 * Contains the business logic to support the login operation.
 */
public class LoginServiceProxy implements LoginService {

    public LoginResponse login(LoginRequest request) throws IOException {
        ServerFacade serverFacade = new ServerFacade();
        LoginResponse loginResponse = serverFacade.login(request);

        if(loginResponse.isSuccess()) {
            loadImage(loginResponse.getUser());
        }

        return loginResponse;
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
}