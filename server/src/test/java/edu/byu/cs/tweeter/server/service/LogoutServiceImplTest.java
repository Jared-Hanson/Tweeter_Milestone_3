package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class LogoutServiceImplTest {

    private LogoutRequest validRequest;
    private LogoutRequest invalidRequest;

    private LogoutResponse successResponse;
    private LogoutResponse failureResponse;

    private LogoutServiceImpl logoutServiceSpy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName", null);
        AuthToken token = new AuthToken();


        // Setup request objects to use in the tests
        validRequest = new LogoutRequest(currentUser, token);
        invalidRequest = new LogoutRequest(null, null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new LogoutResponse(true, "success");
        UserDAO mockUserDao = Mockito.mock(UserDAO.class);
        Mockito.when(mockUserDao.logout(validRequest)).thenReturn(successResponse);

        failureResponse = new LogoutResponse(false, "An exception occurred");
        Mockito.when(mockUserDao.logout(invalidRequest)).thenReturn(failureResponse);

        logoutServiceSpy = Mockito.spy(new LogoutServiceImpl());
        Mockito.when(logoutServiceSpy.getUserDAO()).thenReturn(mockUserDao);

        AuthTokenDAO mockAuthTokenDao = Mockito.mock(AuthTokenDAO.class);
        Mockito.when(logoutServiceSpy.getAuthTokenDAO()).thenReturn(mockAuthTokenDao);
    }

    @Test
    public void testLogout_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        LogoutResponse response = logoutServiceSpy.logout(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testLogout_invalidRequest_returnsFailure() throws IOException, TweeterRemoteException {
        LogoutResponse response = logoutServiceSpy.logout(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
