package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowDataRequest;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.model.service.response.FollowDataResponse;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.model.service.response.FollowingResponse;
import edu.byu.cs.tweeter.server.dao.FollowingDAO;

public class FollowDataServiceImplTest {

    private FollowDataRequest request;
    private FollowDataResponse expectedResponse;
    private FollowingDAO mockFollowingDAO;
    private FollowDataServiceImpl followingServiceImplSpy;

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
        request = new FollowDataRequest(currentUser);

        // Setup a mock FollowingDAO that will return known responses
        expectedResponse = new FollowDataResponse(2, 4);
        mockFollowingDAO = Mockito.mock(FollowingDAO.class);
        Mockito.when(mockFollowingDAO.getFollowerCount(request.getUser())).thenReturn(2);
        Mockito.when(mockFollowingDAO.getFolloweeCount(request.getUser())).thenReturn(4);

        followingServiceImplSpy = Mockito.spy(FollowDataServiceImpl.class);
        Mockito.when(followingServiceImplSpy.getFollowerDAO()).thenReturn(mockFollowingDAO);
        Mockito.when(followingServiceImplSpy.getFollowingDAO()).thenReturn(mockFollowingDAO);
    }

    /**
     * Verify that the {@link FollowingServiceImpl#getFollowees(FollowingRequest)}
     * method returns the same result as the {@link FollowingDAO} class.
     */
    @Test
    public void test_GetData_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FollowDataResponse response = followingServiceImplSpy.getFollowerData(request);
        Assertions.assertEquals(expectedResponse, response);
    }
}
