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
import edu.byu.cs.tweeter.server.dao.StoryDAO;

public class GetStoryServiceImplTest {
    private StoryResponse expectedResponse;
    private StoryRequest request;
    private StoryDAO mMockStoryDAO;
    private StoryServiceImpl storyServiceSpy;

    @BeforeEach
    public void setup() {
        User currentUser = new User("FirstName", "LastName", null);
        LocalDate date1 = LocalDate.of(2021, 1, 8);
        LocalDate date2 = LocalDate.of(2021, 2, 24);
        LocalDate date3 = LocalDate.of(2021, 3, 28);

        Tweet tweet1 = new Tweet(currentUser, "This is a tweet", date1.toEpochDay());
        Tweet tweet2 = new Tweet(currentUser, "This is another tweet", date2.toEpochDay());
        Tweet tweet3 = new Tweet(currentUser, "This is yet another tweet", date3.toEpochDay());

        request = new StoryRequest(currentUser.getAlias(), 3, null);

        expectedResponse = new StoryResponse(Arrays.asList(tweet1, tweet2, tweet3), false);
        mMockStoryDAO = Mockito.mock(StoryDAO.class);
        Mockito.when(mMockStoryDAO.getStory(request)).thenReturn(expectedResponse);

        storyServiceSpy = Mockito.spy(StoryServiceImpl.class);
        Mockito.when(storyServiceSpy.getTweetDAO()).thenReturn(mMockStoryDAO);
    }

    @Test
    public void testGetStory_validRequest_correctResponse() {
        StoryResponse response = storyServiceSpy.getStory(request);
        Assertions.assertEquals(expectedResponse, response);
    }
}
