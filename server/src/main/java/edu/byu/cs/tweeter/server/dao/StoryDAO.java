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
}