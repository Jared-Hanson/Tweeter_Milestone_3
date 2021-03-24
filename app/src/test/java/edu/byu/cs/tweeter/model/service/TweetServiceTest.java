package edu.byu.cs.tweeter.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;

import edu.byu.cs.tweeter.client.model.net.ServerFacade_For_M3;
import edu.byu.cs.tweeter.client.model.service.TweetServiceProxy;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.TweetRequest;
import edu.byu.cs.tweeter.model.service.response.TweetResponse;

public class TweetServiceTest {

    private TweetRequest validRequest;
    private TweetRequest invalidRequest;

    private TweetResponse successResponse;
    private TweetResponse failureResponse;

    private TweetServiceProxy tweetServiceSpy;

    private final String URL_PATH = "/posttweet";

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName", null);

        LocalDate time = LocalDate.of(2021, 1, 8);

        // Setup request objects to use in the tests
        validRequest = new TweetRequest(currentUser, "tweet", time, new AuthToken());
        invalidRequest = new TweetRequest(null, null, null, null);

        // Setup a mock ServerFacade that will return known responses
        successResponse = new TweetResponse(true);
        ServerFacade_For_M3 mockServerFacade = Mockito.mock(ServerFacade_For_M3.class);
        Mockito.when(mockServerFacade.postTweet(validRequest, URL_PATH)).thenReturn(successResponse);

        failureResponse = new TweetResponse(false);
        Mockito.when(mockServerFacade.postTweet(invalidRequest, URL_PATH)).thenReturn(failureResponse);

        tweetServiceSpy = Mockito.spy(new TweetServiceProxy());
        Mockito.when(tweetServiceSpy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void testPostTweet_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        TweetResponse response = tweetServiceSpy.postTweet(validRequest);
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void testPostTweet_invalidRequest_returnsNoSuccess() throws IOException, TweeterRemoteException {
        TweetResponse response = tweetServiceSpy.postTweet(invalidRequest);
        Assertions.assertEquals(failureResponse, response);
    }
}
