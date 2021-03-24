package edu.byu.cs.tweeter.model.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.LocalDate;

import edu.byu.cs.tweeter.client.model.service.TweetServiceProxy;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Tweet;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.TweetRequest;
import edu.byu.cs.tweeter.model.service.response.TweetResponse;

public class Integration_TweetTest {

    private TweetRequest request;
    private TweetResponse expectedResponse;
    private TweetServiceProxy tweetServiceSpy;

    @BeforeEach
    public void setup() {
        User currentUser = new User("FirstName", "LastName", null);
        LocalDate date1 = LocalDate.of(2021, 1, 8);
        Tweet tweet1 = new Tweet(currentUser, "This is a tweet", date1);

        request = new TweetRequest(currentUser, tweet1.getBody(), date1.toString(), new AuthToken());

        expectedResponse = new TweetResponse(true);

        tweetServiceSpy = Mockito.spy(new TweetServiceProxy());

    }

    @Test
    public void testPostTweet_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        TweetResponse response = tweetServiceSpy.postTweet(request);
        Assertions.assertEquals(expectedResponse, response);
    }
}
