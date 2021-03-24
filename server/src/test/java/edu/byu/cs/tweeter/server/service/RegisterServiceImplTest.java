package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class RegisterServiceImplTest {
    private RegisterRequest validRequest;
    private RegisterRequest invalidRequest;

    private LoginResponse successResponse;
    private LoginResponse failureResponse;

    private RegisterServiceImpl registerServiceSpy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        AuthToken token = new AuthToken();
        String login = "@FirstNameLastName";
        String password = "password";


        // Setup request objects to use in the tests
        validRequest = new RegisterRequest("FirstName", "LastName", login, password, null);
        invalidRequest = new RegisterRequest(null, null, null, null, null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new LoginResponse(currentUser, token);
        UserDAO mockUserDao = Mockito.mock(UserDAO.class);
        Mockito.when(mockUserDao.register(validRequest)).thenReturn(successResponse);

        failureResponse = new LoginResponse("An exception occurred");
        Mockito.when(mockUserDao.register(invalidRequest)).thenReturn(failureResponse);

        registerServiceSpy = Mockito.spy(new RegisterServiceImpl());
        Mockito.when(registerServiceSpy.getUserDAO()).thenReturn(mockUserDao);

        AuthTokenDAO mockAuthTokenDao = Mockito.mock(AuthTokenDAO.class);
        Mockito.when(registerServiceSpy.getAuthTokenDAO()).thenReturn(mockAuthTokenDao);
    }

    @Test
    public void testRegister_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        LoginResponse response = registerServiceSpy.register(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testRegister_invalidRequest_returnsFailure() throws IOException, TweeterRemoteException {
        LoginResponse response = registerServiceSpy.register(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
