package edu.byu.cs.tweeter.server.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;

import edu.byu.cs.tweeter.model.domain.Tweet;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.GetUserDataRequest;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.request.TweetRequest;
import edu.byu.cs.tweeter.model.service.response.GetUserDataResponse;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;
import edu.byu.cs.tweeter.model.service.response.TweetResponse;

public class StoryDAO {
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


    private final LocalDate date4 = LocalDate.of(2021, 3, 15);
    private final LocalDate date5 = LocalDate.of(2020, 1, 18);
    private final LocalDate date6 = LocalDate.of(2020, 1, 20);
    private final LocalDate date7 = LocalDate.of(2021, 2, 21);
    private final LocalDate date8 = LocalDate.of(2019, 5, 8);

    private final Tweet fTweet1 = new Tweet(user3, "I hate dummy data", date4.toEpochDay());
    private final Tweet fTweet2 = new Tweet(user5, "Who did that?", date5.toEpochDay());
    private final Tweet fTweet3 = new Tweet(user9, "Visit my page at " + user9.getAlias(), date6.toEpochDay());
    private final Tweet fTweet4 = new Tweet(user1, "Go to https://www.byu.edu/", date7.toEpochDay());
    private final Tweet fTweet5 = new Tweet(user8, "Visit my page at (Not Someone you follow)" + user8.getAlias(), date8.toEpochDay());

    private final List<Tweet> loginFeed = new ArrayList<Tweet>(Arrays.asList(fTweet2, fTweet3, fTweet4,
            fTweet5, fTweet1));

    private final List<Tweet> registerFeed = Collections.emptyList();

    public TweetResponse postTweet(TweetRequest request) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion("us-west-2")
                .build();

        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable("story");

        PutItemOutcome outcome;

        try {
            outcome = table
                    .putItem(new Item().withPrimaryKey("user_alias", request.getUser(),
                            "epoch_date", request.getDate())
                            .withString("tweet_body", request.getTweetBody()));
        } catch (Exception e) {
            return new TweetResponse(false, "Post tweet failed");
        }

        if (outcome == null) {
            return new TweetResponse(false, "Post tweet failed");
        }

        TweetResponse response = new TweetResponse(true);
        return response;
    }

    public StoryResponse getStory(StoryRequest request) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion("us-west-2")
                .build();

        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable("story");

        HashMap<String, String> nameMap = new HashMap<String, String>();
        nameMap.put("#ua", "user_alias");
        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":alias", request.getUserAlias());

        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("#ua = :alias")
                .withNameMap(nameMap)
                .withValueMap(valueMap)
                .withScanIndexForward(false)
                .withMaxResultSize(request.getLimit());

        ItemCollection<QueryOutcome> items = null;
        List<Tweet> tweets = new ArrayList<>();
        Boolean morePages = false;

        try {
            if (request.getLastTweet() != null) {
                querySpec.withExclusiveStartKey("user_alias",
                        request.getLastTweet().getAuthor().getAlias(),
                        "epoch_date", request.getLastTweet().getDate());
            }
            items = table.query(querySpec);
            Iterator<Item> iterator=items.iterator();
            Item item = null;
            UserDAO userDAO = new UserDAO();
            while (iterator.hasNext()) {
                item=iterator.next();
                GetUserDataRequest tweetPosterRequest = new GetUserDataRequest(item.getString("user_alias"));
                GetUserDataResponse tweetPoster = userDAO.getUserFromAlias(tweetPosterRequest);
                tweets.add(new Tweet(tweetPoster.getUser(),
                        item.getString("tweet_body"),
                        item.getLong("epoch_date")));
            }

            if (item != null) {
                if (items.getLastLowLevelResult().getQueryResult().getLastEvaluatedKey() != null) {
                    morePages = true;
                }
            }
        }
        catch (Exception e) {
            System.out.println("EXCEPTION THROWN: " + e.getMessage());
            return new StoryResponse("Could not retrieve story");
        }

        return new StoryResponse(tweets, morePages);
    }

    public StoryResponse getFeed(StoryRequest request) {

        assert request.getLimit() > 0;
        assert request.getUserAlias() != null;

        List<Tweet> allTweets = getDummyTweetsFeed(request.getUserAlias());
        List<Tweet> responseTweets = new ArrayList<>(request.getLimit());

        Collections.sort(allTweets);

        boolean hasMorePages = false;

        if (request.getLimit() > 0) {
            int tweetIndex = getTweetStartingIndex(request.getLastTweet(), allTweets);

            for (int limitCounter = 0; tweetIndex < allTweets.size() && limitCounter < request.getLimit(); tweetIndex++, limitCounter++) {
                responseTweets.add(allTweets.get(tweetIndex));
            }

            hasMorePages = tweetIndex < allTweets.size();
        }

        return new StoryResponse(true, responseTweets, hasMorePages);
    }

    private int getTweetStartingIndex(Tweet lastTweet, List<Tweet> allTweets) {

        int tweetIndex = 0;

        if (lastTweet != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allTweets.size(); i++) {
                if (lastTweet.equals(allTweets.get(i))) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    tweetIndex = i + 1;
                    break;
                }
            }
        }

        return tweetIndex;
    }

    List<Tweet> getDummyTweetsFeed(String alias) {
        if (alias.equals(testUser.getAlias())) {
            return loginFeed;
        } else if (alias.equals(user1.getAlias()) ||
                alias.equals(user2.getAlias()) ||
                alias.equals(user3.getAlias()) ||
                alias.equals(user4.getAlias()) ||
                alias.equals(user5.getAlias()) ||
                alias.equals(user6.getAlias()) ||
                alias.equals(user7.getAlias()) ||
                alias.equals(user8.getAlias()) ||
                alias.equals(user9.getAlias()) ||
                alias.equals(user10.getAlias()) ||
                alias.equals(user11.getAlias()) ||
                alias.equals(user12.getAlias()) ||
                alias.equals(user13.getAlias()) ||
                alias.equals(user14.getAlias()) ||
                alias.equals(user15.getAlias()) ||
                alias.equals(user16.getAlias()) ||
                alias.equals(user17.getAlias()) ||
                alias.equals(user18.getAlias()) ||
                alias.equals(user19.getAlias()) ||
                alias.equals(user20.getAlias())) {
            return loginFeed;
        } else {
            return registerFeed;
        }
    }
}