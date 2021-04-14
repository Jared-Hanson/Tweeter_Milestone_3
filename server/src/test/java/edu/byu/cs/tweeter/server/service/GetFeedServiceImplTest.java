package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Arrays;

import edu.byu.cs.tweeter.model.domain.Tweet;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.FeedDAO;
import edu.byu.cs.tweeter.server.dao.StoryDAO;

public class GetFeedServiceImplTest {
    private StoryResponse expectedResponse;
    private StoryRequest request;
    private FeedDAO mMockFeedDAO;
    private FeedServiceImpl feedServiceSpy;

    @BeforeEach
    public void setup() {
        User currentUser = new User("FirstName", "LastName", null);
        LocalDate date1 = LocalDate.of(2021, 1, 8);
        LocalDate date2 = LocalDate.of(2021, 2, 24);
        LocalDate date3 = LocalDate.of(2021, 3, 28);
        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User resultUser2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User resultUser3 = new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        Tweet tweet1 = new Tweet(resultUser1, "This is a tweet", date1.toEpochDay());
        Tweet tweet2 = new Tweet(resultUser2, "This is another tweet", date2.toEpochDay());
        Tweet tweet3 = new Tweet(resultUser3, "This is yet another tweet", date3.toEpochDay());

        request = new StoryRequest(currentUser.getAlias(), 3, null);

        expectedResponse = new StoryResponse(Arrays.asList(tweet1, tweet2, tweet3), false);
        mMockFeedDAO = Mockito.mock(FeedDAO.class);
        Mockito.when(mMockFeedDAO.getFeed(request)).thenReturn(expectedResponse);

        feedServiceSpy = Mockito.spy(FeedServiceImpl.class);
        Mockito.when(feedServiceSpy.getTweetDAO()).thenReturn(mMockFeedDAO);
    }

    @Test
    public void testGetFeed_validRequest_correctResponse() {
        StoryResponse response = feedServiceSpy.getFeed(request);
        Assertions.assertEquals(expectedResponse, response);
    }
}
