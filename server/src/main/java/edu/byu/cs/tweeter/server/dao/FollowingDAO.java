package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Tweet;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FollowActionRequest;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.model.service.request.GetUserDataRequest;
import edu.byu.cs.tweeter.model.service.response.FollowActionResponse;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.model.service.response.FollowingResponse;
import edu.byu.cs.tweeter.model.service.response.GetUserDataResponse;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;

/**
 * A DAO for accessing 'following' data from the database.
 */
public class FollowingDAO {
    // This is the hard coded followee data returned by the 'getFollowees()' method
    private static final String MALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private static final String FEMALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png";

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

    private List<User> loginFollowees = Arrays.asList(user1, user2, user3, user4, user5, user6, user7,
            user9, user10, user11, user12, user13, user14, user15, user16, user17, user18,
            user19, user20);

    /**
     * Gets the count of users from the database that the user specified is following. The
     * current implementation uses generated data and doesn't actually access a database.
     *
     * @param follower the User whose count of how many following is desired.
     * @return said count.
     */
    public Integer getFolloweeCount(User follower) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion("us-west-2")
                .build();
        int length = 0;
        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable("follows");

        HashMap<String, String> nameMap = new HashMap<String, String>();
        nameMap.put("#ua", "follower_handle");
        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":alias", follower.getAlias());

        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("#ua = :alias")
                .withNameMap(nameMap)
                .withValueMap(valueMap)
                .withScanIndexForward(false);


        ItemCollection<QueryOutcome> items = null;
        List<User> users = new ArrayList<>();
        Boolean morePages = false;

        try {
                items = table.query(querySpec);
                Iterator<Item> iterator = items.iterator();
                Item item = null;
                UserDAO userDAO = new UserDAO();
                while (iterator.hasNext()) {
                    item = iterator.next();
                    length += 1;
                }


        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return  -1;
        }

        return length;

    }



    /**
     * Gets the users from the database that the user specified in the request is following. Uses
     * information in the request object to limit the number of followees returned and to return the
     * next set of followees after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(FollowingRequest request) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion("us-west-2")
                .build();

        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable("follows");

        HashMap<String, String> nameMap = new HashMap<String, String>();
        nameMap.put("#ua", "follower_handle");
        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":alias", request.getFollowerAlias());

        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("#ua = :alias")
                .withNameMap(nameMap)
                .withValueMap(valueMap)
                .withScanIndexForward(false)
                .withMaxResultSize(request.getLimit());

        ItemCollection<QueryOutcome> items = null;
        List<User> users = new ArrayList<>();
        Boolean morePages = false;

        try {
            if (request.getLastFolloweeAlias() != null) {
                querySpec.withExclusiveStartKey("follower_handle", request.getFollowerAlias(),
                        "followee_handle", request.getLastFolloweeAlias());
            }
            items = table.query(querySpec);
            Iterator<Item> iterator=items.iterator();
            Item item = null;
            UserDAO userDAO = new UserDAO();
            while (iterator.hasNext()) {
                item=iterator.next();
                GetUserDataRequest userPosterRequest = new GetUserDataRequest(item.getString("followee_handle"));
                GetUserDataResponse userPoster = userDAO.getUserFromAlias(userPosterRequest);
                users.add(userPoster.getUser());
            }

            if (item != null) {
                if (items.getLastLowLevelResult().getQueryResult().getLastEvaluatedKey() != null) {
                    morePages = true;
                }
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new FollowingResponse("Could not retrieve feed");
        }

        return new FollowingResponse(users, morePages);
    }



    public Integer getFollowerCount(User follower) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion("us-west-2")
                .build();
        int length = 0;
        DynamoDB dynamoDB = new DynamoDB(client);

        Index table = dynamoDB.getTable("follows").getIndex("follows_index");

        HashMap<String, String> nameMap = new HashMap<String, String>();
        nameMap.put("#ua", "followee_handle");
        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":alias", follower.getAlias());

        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("#ua = :alias")
                .withNameMap(nameMap)
                .withValueMap(valueMap)
                .withScanIndexForward(false);


        ItemCollection<QueryOutcome> items = null;
        List<User> users = new ArrayList<>();
        Boolean morePages = false;

        try {
            items = table.query(querySpec);
            Iterator<Item> iterator = items.iterator();
            Item item = null;
            UserDAO userDAO = new UserDAO();
            while (iterator.hasNext()) {
                item = iterator.next();
                length += 1;
            }


        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return  -1;
        }

        return length;
    }

    /**
     * Gets the users from the database that the user specified in the request is following. Uses
     * information in the request object to limit the number of followees returned and to return the
     * next set of followees after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the followees.
     */
    public FollowerResponse getFollowers(FollowerRequest request) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion("us-west-2")
                .build();

        DynamoDB dynamoDB = new DynamoDB(client);


        Index index = dynamoDB.getTable("follows").getIndex("follows_index");

        HashMap<String, String> nameMap = new HashMap<String, String>();
        nameMap.put("#ua", "followee_handle");
        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":alias", request.getFollowerAlias());

        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("#ua = :alias")
                .withNameMap(nameMap)
                .withValueMap(valueMap)
                .withScanIndexForward(false)
                .withMaxResultSize(request.getLimit());

        ItemCollection<QueryOutcome> items = null;
        List<User> users = new ArrayList<>();
        Boolean morePages = false;

        try {
            if (request.getLastFolloweeAlias() != null) {
                querySpec.withExclusiveStartKey("followee_handle", request.getFollowerAlias(),
                        "follower_handle", request.getLastFolloweeAlias());
            }
            items = index.query(querySpec);
            Iterator<Item> iterator=items.iterator();
            Item item = null;
            UserDAO userDAO = new UserDAO();
            while (iterator.hasNext()) {
                item=iterator.next();
                GetUserDataRequest userPosterRequest = new GetUserDataRequest(item.getString("follower_handle"));
                GetUserDataResponse userPoster = userDAO.getUserFromAlias(userPosterRequest);
                users.add(userPoster.getUser());
            }

            if (item != null) {
                if (items.getLastLowLevelResult().getQueryResult().getLastEvaluatedKey() != null) {
                    morePages = true;
                }
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new FollowerResponse("Could not retrieve feed");
        }

        return new FollowerResponse(users, morePages);
    }



    public FollowActionResponse followUser(FollowActionRequest request){
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion("us-west-2")
                .build();

        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable("follows");

        PutItemOutcome outcome;



        try {
            //fix, set proper alias
            outcome = table
                    .putItem(new Item().withPrimaryKey("follower_handle", request.getUser().getAlias(),
                            "followee_handle", request.getUserToFollow().getAlias())
                            .withString( "followee_name", request.getUserToFollow().getFirstName())
                            .withString("follower_name", request.getUser().getFirstName()));
        } catch (Exception e) {
            return new FollowActionResponse(false, e.getMessage());
        }

        if (outcome == null) {
            return new FollowActionResponse(false, "Post tweet failed2");
        }

        FollowActionResponse response = new FollowActionResponse(true,"well done, it worked");
        return response;
    }

    public FollowActionResponse unFollowUser(FollowActionRequest request){
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion("us-west-2")
                .build();

        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable("follows");

        DeleteItemOutcome outcome;
        DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                .withPrimaryKey(new PrimaryKey("follower_handle", request.getUser().getAlias(), "followee_handle", request.getUserToFollow().getAlias()));

        try {
            //fix, set proper alias
            outcome = table.deleteItem(deleteItemSpec);
        } catch (Exception e) {
            return new FollowActionResponse(false, e.getMessage());
        }

        if (outcome == null) {
            return new FollowActionResponse(false, "Post tweet failed2");
        }

        FollowActionResponse response = new FollowActionResponse(true,"well done, it worked");
        return response;
    }
    public FollowActionResponse isFollowing(FollowActionRequest request){
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion("us-west-2")
                .build();

        DynamoDB dynamoDB = new DynamoDB(client);


        Table index = dynamoDB.getTable("follows");

        HashMap<String, String> nameMap = new HashMap<String, String>();
        nameMap.put("#ua", "follower_handle");
        nameMap.put("#fa", "followee_handle");
        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":alias", request.getUser().getAlias());
        valueMap.put(":Falias", request.getUserToFollow().getAlias());

        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("#ua = :alias and #fa = :Falias")
                .withNameMap(nameMap)
                .withValueMap(valueMap)
                .withScanIndexForward(false);

        ItemCollection<QueryOutcome> items = null;


        boolean hasValues;
        try {

            items = index.query(querySpec);
            Iterator<Item> iterator =items.iterator();


            if (iterator.hasNext()) {
                hasValues = true;
            }
            else {
                hasValues = false;
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new FollowActionResponse(false, "Could not retrieve feed");
        }

        return new FollowActionResponse(hasValues, "retreaved value");

    }


    List<User> getDummyFollowees() {
        return Arrays.asList(user1, user2, user3, user4, user5, user6, user7,
                user8, user9, user10, user11, user12, user13, user14, user15, user16, user17, user18,
                user19, user20);
    }
}
