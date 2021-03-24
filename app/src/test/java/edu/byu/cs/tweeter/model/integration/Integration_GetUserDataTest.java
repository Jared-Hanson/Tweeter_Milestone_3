package edu.byu.cs.tweeter.model.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade_For_M3;
import edu.byu.cs.tweeter.client.model.service.GetUserDataServiceProxy;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.GetUserDataRequest;
import edu.byu.cs.tweeter.model.service.response.GetUserDataResponse;

public class Integration_GetUserDataTest {

    private GetUserDataRequest validRequest;
    private GetUserDataRequest invalidRequest;

    private GetUserDataResponse successResponse;
    private GetUserDataResponse failureResponse;

    private GetUserDataServiceProxy getUserDataService;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        String goodAlias = "@AllenAnderson";
        String badAlias = "Gregory";
        User currentUser = new User("Allen", "Anderson",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        // Setup request objects to use in the tests
        validRequest = new GetUserDataRequest(goodAlias);
        invalidRequest = new GetUserDataRequest(badAlias);

        successResponse = new GetUserDataResponse(currentUser);
        failureResponse = new GetUserDataResponse(false, null);

        getUserDataService = new GetUserDataServiceProxy();
    }

    @Test
    public void testGetUserData_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        GetUserDataResponse response = getUserDataService.getUserData(validRequest);
        Assertions.assertEquals(successResponse.getUser(), response.getUser());
    }

    @Test
    public void testGetUserData_validRequest_loadsProfileImages() throws IOException, TweeterRemoteException {
        GetUserDataResponse response = getUserDataService.getUserData(validRequest);

        Assertions.assertNotNull(response.getUser().getImageBytes());
    }

    @Test
    public void testGetUserData_invalidRequest_returnsNoData() throws IOException, TweeterRemoteException {
        Assertions.assertThrows(RuntimeException.class, () -> getUserDataService.getUserData(invalidRequest));
    }
}
