package edu.byu.cs.tweeter.model.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade_For_M3;
import edu.byu.cs.tweeter.client.model.service.LoginServiceProxy;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;

public class Integration_LoginTest {

    private LoginRequest validRequest;
    private LoginRequest invalidRequest;

    private LoginResponse successResponse;
    private LoginResponse failureResponse;

    private LoginServiceProxy loginService;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("Test", "User", "@dummyUserName","https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        AuthToken token = new AuthToken();
        String goodLogin = "d";
        String badLogin = "Gregory";
        String password = "dddddddd";


        // Setup request objects to use in the tests
        validRequest = new LoginRequest(goodLogin, password);
        invalidRequest = new LoginRequest(badLogin, password);

        successResponse = new LoginResponse(currentUser, token);
        failureResponse = new LoginResponse("An exception occurred");

        loginService = new LoginServiceProxy();
    }

    @Test
    public void testLogin_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        LoginResponse response = loginService.login(validRequest);
        Assertions.assertEquals(successResponse.getUser(), response.getUser());
        Assertions.assertNotNull(response.getAuthToken());
    }

    @Test
    public void testLogin_invalidRequest_returnsFailure() throws IOException, TweeterRemoteException {
        Assertions.assertThrows(RuntimeException.class, () -> loginService.login(invalidRequest));
    }
}
