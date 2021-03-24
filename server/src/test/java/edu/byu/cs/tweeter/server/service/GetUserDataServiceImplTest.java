package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.GetUserDataRequest;
import edu.byu.cs.tweeter.model.service.response.GetUserDataResponse;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class GetUserDataServiceImplTest {

    private GetUserDataRequest validRequest;
    private GetUserDataRequest invalidRequest;

    private GetUserDataResponse successResponse;
    private GetUserDataResponse failureResponse;

    private GetUserDataServiceImpl getUserDataServiceSpy;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        String goodAlias = "@FirstNameLastName";
        String badAlias = "Gregory";
        User currentUser = new User("FirstName", "LastName",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        // Setup request objects to use in the tests
        validRequest = new GetUserDataRequest(goodAlias);
        invalidRequest = new GetUserDataRequest(badAlias);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new GetUserDataResponse(currentUser);
        UserDAO mockUserDao = Mockito.mock(UserDAO.class);
        Mockito.when(mockUserDao.getUserFromAlias(validRequest)).thenReturn(successResponse);

        failureResponse = new GetUserDataResponse("An exception occurred");
        Mockito.when(mockUserDao.getUserFromAlias(invalidRequest)).thenReturn(failureResponse);

        getUserDataServiceSpy = Mockito.spy(new GetUserDataServiceImpl());
        Mockito.when(getUserDataServiceSpy.getUserDAO()).thenReturn(mockUserDao);
    }

    @Test
    public void testGetUserData_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        GetUserDataResponse response = getUserDataServiceSpy.getUserData(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testGetUserData_invalidRequest_returnsNoData() throws IOException, TweeterRemoteException {
        GetUserDataResponse response = getUserDataServiceSpy.getUserData(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
