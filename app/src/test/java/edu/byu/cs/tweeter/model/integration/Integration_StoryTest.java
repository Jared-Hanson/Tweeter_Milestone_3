package edu.byu.cs.tweeter.model.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.byu.cs.tweeter.client.model.service.StoryServiceProxy;
import edu.byu.cs.tweeter.model.domain.Tweet;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;

public class Integration_StoryTest {
    // This is the hard coded followee data returned by the 'getFollowees()' method
    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private static final String FEMALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png";

    //private final User testUser = new User("Test", "User", "@dummyUserName", MALE_IMAGE_URL);

    private final User user1 = new User("Allen", "Anderson", MALE_IMAGE_URL);
    private final User user2 = new User("Amy", "Ames", FEMALE_IMAGE_URL);
    private final User user3 = new User("Bob", "Bobson", MALE_IMAGE_URL);
    private final User user4 = new User("Bonnie", "Beatty", FEMALE_IMAGE_URL);
    private final User user5 = new User("Chris", "Colston", MALE_IMAGE_URL);
    private final User user6 = new User("Cindy", "Coats", FEMALE_IMAGE_URL);
    private final User user7 = new User("Dan", "Donaldson", MALE_IMAGE_URL);
    private final User user8 = new User("Dee", "Dempsey", FEMALE_IMAGE_URL);
    private final User user9 = new User("Elliott", "Enderson", MALE_IMAGE_URL);
    private final User user10 = new User("Elizabeth", "Engle", FEMALE_IMAGE_URL);
    private final User user11 = new User("Frank", "Frandson", MALE_IMAGE_URL);
    private final User user12 = new User("Fran", "Franklin", FEMALE_IMAGE_URL);
    private final User user13 = new User("Gary", "Gilbert", MALE_IMAGE_URL);
    private final User user14 = new User("Giovanna", "Giles", FEMALE_IMAGE_URL);
    private final User user15 = new User("Henry", "Henderson", MALE_IMAGE_URL);
    private final User user16 = new User("Helen", "Hopwell", FEMALE_IMAGE_URL);
    private final User user17 = new User("Igor", "Isaacson", MALE_IMAGE_URL);
    private final User user18 = new User("Isabel", "Isaacson", FEMALE_IMAGE_URL);
    private final User user19 = new User("Justin", "Jones", MALE_IMAGE_URL);
    private final User user20 = new User("Jill", "Johnson", FEMALE_IMAGE_URL);


    private final List<User> loginFollowees = Arrays.asList(user1, user2, user3, user4, user5, user6, user7,
            user9, user10, user11, user12, user13, user14, user15, user16, user17, user18,
            user19, user20);
    private final List<User> loginFollowers = Arrays.asList(user3, user4, user5, user6, user7, user15,
            user16, user17, user18, user19, user20);
    private final User testUser = new User("Test", "User", "@dummyUserName", MALE_IMAGE_URL, loginFollowers.size(), loginFollowees.size());
    private final LocalDate date1 = LocalDate.of(2021, 1, 8);
    private final LocalDate date2 = LocalDate.of(2021, 2, 9);
    private final LocalDate date3 = LocalDate.of(2020, 2, 11);
    private final LocalDate date4 = LocalDate.of(2021, 3, 15);
    private final LocalDate date5 = LocalDate.of(2020, 1, 18);
    private final LocalDate date6 = LocalDate.of(2020, 1, 20);
    private final LocalDate date7 = LocalDate.of(2021, 2, 21);
    private final LocalDate date8 = LocalDate.of(2019, 5, 8);
    private final Tweet uTweet1 = new Tweet(testUser, "what a tweet eh", date1);
    private final Tweet uTweet2 = new Tweet(testUser, "Second Tweet" + user9.getAlias(), date2);
    private final Tweet uTweet3 = new Tweet(testUser, "What https://nba.com a tweet https://byu.edu", date3);
    private final Tweet fTweet1 = new Tweet(user3, "I hate dummy data", date4);
    private final Tweet fTweet2 = new Tweet(user5, "Who did that?", date5);
    private final Tweet fTweet3 = new Tweet(user9, "Visit my page at " + user9.getAlias(), date6);
    private final Tweet fTweet4 = new Tweet(user1, "Go to https://www.byu.edu/", date7);
    private final Tweet fTweet5 = new Tweet(user8, "Visit my page at (Not Someone you follow)" + user8.getAlias(), date8);

    private StoryResponse expectedResponse;
    private StoryRequest request;
    private StoryServiceProxy storyServiceSpy;
    User currentUser = new User("FirstName", "LastName", null);


    @BeforeEach
    public void setup() {

        request = new StoryRequest(currentUser.getAlias(), 3, null);

        expectedResponse = getStory(request);

        storyServiceSpy = Mockito.spy(new StoryServiceProxy());
    }

    @Test
    public void testGetStory_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        StoryResponse response = storyServiceSpy.getStory(request);
        Assertions.assertEquals(expectedResponse, response);
    }

    public StoryResponse getStory(StoryRequest request) {
        assert request.getLimit() > 0;
        assert request.getUserAlias() != null;

        List<Tweet> allTweets = getDummyStory();
        List<Tweet> responseTweets = new ArrayList<>(request.getLimit());

        Collections.sort(allTweets);

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            int tweetIndex = getTweetStartingIndex(request.getLastTweet(), allTweets);

            for(int limitCounter = 0; tweetIndex < allTweets.size() && limitCounter < request.getLimit(); tweetIndex++, limitCounter++) {
                responseTweets.add(allTweets.get(tweetIndex));
            }

            hasMorePages = tweetIndex < allTweets.size();
        }

        return new StoryResponse(true, responseTweets, hasMorePages);
    }

    private int getTweetStartingIndex(Tweet lastTweet, List<Tweet> allTweets) {

        int tweetIndex = 0;

        if(lastTweet != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allTweets.size(); i++) {
                if(lastTweet.equals(allTweets.get(i))) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    tweetIndex = i + 1;
                    break;
                }
            }
        }

        return tweetIndex;
    }

    private List<Tweet> getDummyStory() {
        return Arrays.asList(fTweet1, fTweet2, fTweet3);
    }
}
