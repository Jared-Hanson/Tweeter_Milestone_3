package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.presenter.Follow_UnfollowActionPresenter;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.service.FollowActionServiceProxy;
import edu.byu.cs.tweeter.model.service.request.FollowActionRequest;
import edu.byu.cs.tweeter.model.service.response.FollowActionResponse;

public class Follow_UnfollowActionPresenterTest {

    private FollowActionRequest request;
    private FollowActionResponse response;
    private FollowActionServiceProxy mockFollowActionServiceProxy;
    private Follow_UnfollowActionPresenter presenter;

    @BeforeEach
    public void setup() throws IOException {
        User currentUser = new User("FirstName", "LastName", null);
        User otherUser = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        request = new FollowActionRequest(currentUser, otherUser);
        response = new FollowActionResponse(true);

        // Create a mock FollowingService
        mockFollowActionServiceProxy = Mockito.mock(FollowActionServiceProxy.class);
        Mockito.when(mockFollowActionServiceProxy.followUser(request)).thenReturn(response);
        Mockito.when(mockFollowActionServiceProxy.unFollowUser(request)).thenReturn(response);
        Mockito.when(mockFollowActionServiceProxy.isFollowing(request)).thenReturn(response);

        // Wrap a FollowingPresenter in a spy that will use the mock service.
        presenter = Mockito.spy(new Follow_UnfollowActionPresenter(new Follow_UnfollowActionPresenter.View() {}));
        Mockito.when(presenter.getFollowService()).thenReturn(mockFollowActionServiceProxy);
    }

    @Test
    public void testFollowUser_returnsServiceResult() throws IOException {
        Mockito.when(mockFollowActionServiceProxy.followUser(request)).thenReturn(response);

        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(response, presenter.followUser(request));
    }

    @Test
    public void testUnFollowUser_returnsServiceResult() throws IOException {
        Mockito.when(mockFollowActionServiceProxy.unFollowUser(request)).thenReturn(response);

        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(response, presenter.unFollowUser(request));
    }

    @Test
    public void testIsFollowing_returnsServiceResult() throws IOException {
        Mockito.when(mockFollowActionServiceProxy.isFollowing(request)).thenReturn(response);

        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(response, presenter.isFollowing(request));
    }

    @Test
    public void testFollowUser_serviceThrowsIOException_presenterThrowsIOException() throws IOException {
        Mockito.when(mockFollowActionServiceProxy.followUser(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.followUser(request);
        });
    }

    @Test
    public void testUnFollowUser_serviceThrowsIOException_presenterThrowsIOException() throws IOException {
        Mockito.when(mockFollowActionServiceProxy.unFollowUser(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.unFollowUser(request);
        });
    }

    @Test
    public void testIsFollowing_serviceThrowsIOException_presenterThrowsIOException() throws IOException {
        Mockito.when(mockFollowActionServiceProxy.isFollowing(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.isFollowing(request);
        });
    }
}

