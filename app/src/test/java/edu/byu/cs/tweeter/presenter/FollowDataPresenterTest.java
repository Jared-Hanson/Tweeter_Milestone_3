package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.presenter.FollowerDataPresenter;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.service.FollowDataServiceProxy;
import edu.byu.cs.tweeter.model.service.request.FollowDataRequest;
import edu.byu.cs.tweeter.model.service.response.FollowDataResponse;

public class FollowDataPresenterTest {

    private FollowDataRequest request;
    private FollowDataResponse response;
    private FollowDataServiceProxy mockFollowDataServiceProxy;
    private FollowerDataPresenter presenter;

    @BeforeEach
    public void setup() throws IOException {
        User currentUser = new User("FirstName", "LastName", null);

        request = new FollowDataRequest(currentUser);
        response = new FollowDataResponse(1, 1);

        mockFollowDataServiceProxy = Mockito.mock(FollowDataServiceProxy.class);
        Mockito.when(mockFollowDataServiceProxy.getFollowerData(request)).thenReturn(response);

        presenter = Mockito.spy(new FollowerDataPresenter(new FollowerDataPresenter.View() {}));
        Mockito.when(presenter.getFollowerService()).thenReturn(mockFollowDataServiceProxy);
    }

    @Test
    public void testGetFollowingData_returnsServiceResult() throws IOException {
        Mockito.when(mockFollowDataServiceProxy.getFollowerData(request)).thenReturn(response);

        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(response, presenter.getFollowingData(request));
    }

    @Test
    public void testGetFollowingData_serviceThrowsIOException_presenterThrowsIOException() throws IOException {
        Mockito.when(mockFollowDataServiceProxy.getFollowerData(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.getFollowingData(request);
        });
    }
}
