package edu.byu.cs.tweeter.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade_For_M3;
import edu.byu.cs.tweeter.client.model.service.FollowDataServiceProxy;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowDataRequest;
import edu.byu.cs.tweeter.model.service.response.FollowDataResponse;

public class FollowDataServiceProxyTest {

    private FollowDataRequest validRequest;
    private FollowDataRequest invalidRequest;

    private FollowDataResponse successResponse;
    private FollowDataResponse failureResponse;

    private FollowDataServiceProxy followingServiceSpy;

    static final String URL_PATH = "/getfollowdata";

    /**
     * Create a FollowingService spy that uses a mock ServerFacade to return known responses to
     * requests.
     */
    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        // Setup request objects to use in the tests
        validRequest = new FollowDataRequest(currentUser);
        invalidRequest = new FollowDataRequest(null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new FollowDataResponse(1, 1);
        ServerFacade_For_M3 mockServerFacade = Mockito.mock(ServerFacade_For_M3.class);
        Mockito.when(mockServerFacade.getFollowerData(validRequest, URL_PATH)).thenReturn(successResponse);

        failureResponse = new FollowDataResponse(0, 0);
        Mockito.when(mockServerFacade.getFollowerData(invalidRequest, URL_PATH)).thenReturn(failureResponse);

        // Create a FollowingService instance and wrap it with a spy that will use the mock service
        followingServiceSpy = Mockito.spy(new FollowDataServiceProxy());
        Mockito.when(followingServiceSpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testGetFollowerData_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FollowDataResponse response = followingServiceSpy.getFollowerData(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testGetFollowerData_invalidRequest_returnsNoData() throws IOException, TweeterRemoteException {
        FollowDataResponse response = followingServiceSpy.getFollowerData(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
