package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class LoginServiceImplTest {

    private LoginRequest validRequest;
    private LoginRequest invalidRequest;

    private LoginResponse successResponse;
    private LoginResponse failureResponse;

    private LoginServiceImpl loginServiceSpy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        AuthToken token = new AuthToken(currentUser);
        String goodLogin = "@FirstNameLastName";
        String badLogin = "Gregory";
        String password = "password";


        // Setup request objects to use in the tests
        validRequest = new LoginRequest(goodLogin, password);
        invalidRequest = new LoginRequest(badLogin, password);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new LoginResponse(currentUser, token);
        UserDAO mockUserDao = Mockito.mock(UserDAO.class);
        Mockito.when(mockUserDao.login(validRequest)).thenReturn(successResponse);

        failureResponse = new LoginResponse("An exception occurred");
        Mockito.when(mockUserDao.login(invalidRequest)).thenReturn(failureResponse);

        loginServiceSpy = Mockito.spy(new LoginServiceImpl());
        Mockito.when(loginServiceSpy.getUserDAO()).thenReturn(mockUserDao);

        AuthTokenDAO mockAuthTokenDao = Mockito.mock(AuthTokenDAO.class);
        Mockito.when(loginServiceSpy.getAuthTokenDAO()).thenReturn(mockAuthTokenDao);
    }

    @Test
    public void testLogin_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        LoginResponse response = loginServiceSpy.login(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testLogin_invalidRequest_returnsFailure() throws IOException, TweeterRemoteException {
        LoginResponse response = loginServiceSpy.login(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
