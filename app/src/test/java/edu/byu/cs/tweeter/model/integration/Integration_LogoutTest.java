package edu.byu.cs.tweeter.model.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade_For_M3;
import edu.byu.cs.tweeter.client.model.service.LogoutServiceProxy;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;

import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;

public class Integration_LogoutTest {

    private LogoutRequest validRequest;
    private LogoutRequest invalidRequest;

    private LogoutResponse successResponse;

    private LogoutServiceProxy logoutService;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName", null);
        AuthToken token = new AuthToken(currentUser);


        // Setup request objects to use in the tests
        validRequest = new LogoutRequest(currentUser, token);
        invalidRequest = new LogoutRequest(null, null);

        successResponse = new LogoutResponse(true, "@FirstNameLastName succesfully logged out");

        logoutService = new LogoutServiceProxy();
    }

    @Test
    public void testLogout_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        LogoutResponse response = logoutService.logout(validRequest);
        Assertions.assertEquals(successResponse.getMessage(), response.getMessage());
    }

    @Test
    public void testLogout_invalidRequest_returnsFailure() throws IOException, TweeterRemoteException {
        Assertions.assertThrows(RuntimeException.class, () -> logoutService.logout(invalidRequest));
    }
}
