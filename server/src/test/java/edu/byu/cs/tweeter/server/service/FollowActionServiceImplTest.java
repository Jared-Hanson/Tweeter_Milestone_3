package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowActionRequest;
import edu.byu.cs.tweeter.model.service.request.FollowDataRequest;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.model.service.response.FollowActionResponse;
import edu.byu.cs.tweeter.model.service.response.FollowDataResponse;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.model.service.response.FollowingResponse;
import edu.byu.cs.tweeter.server.dao.FollowingDAO;

public class FollowActionServiceImplTest {

    private FollowActionRequest request;
    private FollowActionResponse expectedResponse;
    private FollowingDAO mockFollowingDAO;
    private FollowActionServiceImpl followingServiceImplSpy;

    @BeforeEach
    public void setup() {
        User currentUser = new User("FirstName", "LastName", null);

        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User resultUser2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User resultUser3 = new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        // Setup a request object to use in the tests
        request = new FollowActionRequest(currentUser, resultUser1);

        // Setup a mock FollowingDAO that will return known responses
        expectedResponse = new FollowActionResponse(true);
        mockFollowingDAO = Mockito.mock(FollowingDAO.class);
        Mockito.when(mockFollowingDAO.followUser(request)).thenReturn(expectedResponse);
        Mockito.when(mockFollowingDAO.unFollowUser(request)).thenReturn(expectedResponse);
        Mockito.when(mockFollowingDAO.isFollowing(request)).thenReturn(expectedResponse);


        followingServiceImplSpy = Mockito.spy(FollowActionServiceImpl.class);
        Mockito.when(followingServiceImplSpy.getDAO()).thenReturn(mockFollowingDAO);

    }

    /**
     * Verify that the {@link FollowingServiceImpl#getFollowees(FollowingRequest)}
     * method returns the same result as the {@link FollowingDAO} class.
     */
    @Test
    public void test_FollowUser_correctResponse() throws IOException, TweeterRemoteException {
        FollowActionResponse response = followingServiceImplSpy.followUser(request);
        Assertions.assertEquals(expectedResponse, response);
    }
    @Test
    public void test_UnFollowUser_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FollowActionResponse response = followingServiceImplSpy.unFollowUser(request);
        Assertions.assertEquals(expectedResponse, response);
    }
    @Test
    public void test_IsFollow_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FollowActionResponse response = followingServiceImplSpy.isFollowing(request);
        Assertions.assertEquals(expectedResponse, response);
    }
}