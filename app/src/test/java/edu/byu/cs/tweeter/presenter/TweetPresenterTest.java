package edu.byu.cs.tweeter.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.LocalDate;

import edu.byu.cs.tweeter.client.model.service.TweetServiceProxy;
import edu.byu.cs.tweeter.client.presenter.TweetPresenter;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.TweetRequest;
import edu.byu.cs.tweeter.model.service.response.TweetResponse;

public class TweetPresenterTest {

    private TweetRequest request;
    private TweetResponse response;
    private TweetServiceProxy mockTweetService;
    private TweetPresenter presenter;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName", null);

        LocalDate time = LocalDate.of(2021, 1, 8);

        request = new TweetRequest(currentUser, "tweet", time);
        response = new TweetResponse(true);

        mockTweetService = Mockito.mock(TweetServiceProxy.class);
        Mockito.when(mockTweetService.postTweet(request)).thenReturn(response);

        presenter = Mockito.spy(new TweetPresenter(new TweetPresenter.View() {}));
        Mockito.when(presenter.getTweetService()).thenReturn(mockTweetService);
    }

    @Test
    public void testPostTweet_returnsServiceResult() throws IOException, TweeterRemoteException {
        Mockito.when(mockTweetService.postTweet(request)).thenReturn(response);

        // Assert that the presenter returns the same response as the service (it doesn't do
        // anything else, so there's nothing else to test).
        Assertions.assertEquals(response, presenter.postTweet(request));
    }

    @Test
    public void testPostTweet_serviceThrowsIOException_presenterThrowsIOException() throws IOException, TweeterRemoteException {
        Mockito.when(mockTweetService.postTweet(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.postTweet(request);
        });
    }
}
