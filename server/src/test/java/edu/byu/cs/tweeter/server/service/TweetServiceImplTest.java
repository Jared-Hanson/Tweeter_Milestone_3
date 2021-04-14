package edu.byu.cs.tweeter.server.service;

import com.amazonaws.services.sqs.AmazonSQS;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Tweet;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.TweetRequest;
import edu.byu.cs.tweeter.model.service.response.TweetResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.StoryDAO;

public class TweetServiceImplTest {
    private TweetResponse expectedResponse;
    private TweetRequest request;
    private StoryDAO mMockStoryDAO;
    private AuthTokenDAO mMockAuthTokenDAO;
    private AmazonSQS mMockAmazonSQS;
    private TweetServiceImpl tweetServiceSpy;

    @BeforeEach
    public void setup() {
        User currentUser = new User("FirstName", "LastName", null);
        LocalDate date1 = LocalDate.of(2021, 1, 8);
        Tweet tweet1 = new Tweet(currentUser, "This is a tweet", date1.toEpochDay());

        request = new TweetRequest(currentUser.getAlias(), tweet1.getBody(), date1.toEpochDay(), new AuthToken(currentUser));

        expectedResponse = new TweetResponse(true);
        mMockStoryDAO = Mockito.mock(StoryDAO.class);
        Mockito.when(mMockStoryDAO.postTweet(request)).thenReturn(expectedResponse);

        mMockAuthTokenDAO = Mockito.mock(AuthTokenDAO.class);
        Mockito.when(mMockAuthTokenDAO.checkToken(Mockito.any())).thenReturn(true);

        mMockAmazonSQS = Mockito.mock(AmazonSQS.class);
        Mockito.when(mMockAmazonSQS.sendMessage(Mockito.any())).thenReturn(null);

        tweetServiceSpy = Mockito.spy(TweetServiceImpl.class);
        Mockito.when(tweetServiceSpy.getStoryDAO()).thenReturn(mMockStoryDAO);
        Mockito.when(tweetServiceSpy.getAuthTokenDAO()).thenReturn(mMockAuthTokenDAO);
        Mockito.when(tweetServiceSpy.testing()).thenReturn(true);
        Mockito.when(tweetServiceSpy.getSQS(true)).thenReturn(mMockAmazonSQS);
    }

    @Test
    public void testPostTweet_validRequest_correctResponse() {
        TweetResponse response = tweetServiceSpy.postTweet(request);
        Assertions.assertEquals(expectedResponse, response);
    }
}
