package edu.byu.cs.tweeter.model.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.RegisterServiceProxy;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;

public class Integration_RegisterTest {

    private RegisterRequest validRequest;
    private RegisterRequest invalidRequest;

    private LoginResponse successResponse;

    private RegisterServiceProxy registerService;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("Test", "User", "d","https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        AuthToken token = new AuthToken();
        String login = "d";
        String password = "dddddddd";


        // Setup request objects to use in the tests
        validRequest = new RegisterRequest("Test", "User", login, password, null);
        invalidRequest = new RegisterRequest(null, null, null, null, null);

        successResponse = new LoginResponse(currentUser, token);

        registerService = new RegisterServiceProxy();
    }

    @Test
    public void testLogin_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        LoginResponse response = registerService.register(validRequest);
        Assertions.assertEquals(successResponse.getUser(), response.getUser());
        Assertions.assertNotNull(response.getAuthToken());
    }

    @Test
    public void testLogin_invalidRequest_returnsFailure() throws IOException, TweeterRemoteException {
        Assertions.assertThrows(RuntimeException.class, () -> registerService.register(invalidRequest));
    }
}
