package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.GetUserDataRequest;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.GetUserDataResponse;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.server.lambda.GetUserDataHandler;
import edu.byu.cs.tweeter.server.service.GetUserDataServiceImpl;

public class UserDAO {
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


    private List<User> loginFollowees = Arrays.asList(user1, user2, user3, user4, user5, user6, user7,
            user9, user10, user11, user12, user13, user14, user15, user16, user17, user18,
            user19, user20);
    private List<User> allFollowees = Arrays.asList(user1, user2, user3, user4, user5, user6, user7, user8,
            user9, user10, user11, user12, user13, user14, user15, user16, user17, user18,
            user19, user20);

    private final List<User> loginFollowers = Arrays.asList(user3, user4, user5, user6, user7, user15,
            user16, user17, user18, user19, user20);
    private final User testUser = new User("Test", "User", "@dummyUserName", MALE_IMAGE_URL, loginFollowers.size(), loginFollowees.size());

    public LoginResponse login(LoginRequest request) {

        if(request.getUsername().equals("d") && request.getPassword().equals("dddddddd")) {
            return new LoginResponse(testUser, new AuthToken(testUser));
        }

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion("us-west-2")
                .build();

        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable("user");

        HashMap<String, String> nameMap = new HashMap<>();
        nameMap.put("#a", "alias");
        HashMap<String, Object> valueMap = new HashMap<>();
        valueMap.put(":alias", request.getUsername());

        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("#a = :alias")
                .withNameMap(nameMap)
                .withValueMap(valueMap)
                .withScanIndexForward(false)
                .withMaxPageSize(1);

        ItemCollection<QueryOutcome> items = null;

        try {
            items = table.query(querySpec);
            Iterator<Item> iterator = items.iterator();
            Item item;
            boolean validPass = false;
            int count = 0;
            while(iterator.hasNext()) {
                count++;
                item = iterator.next();
                String hashed = hashPassword(request.getPassword());
                System.out.println(item.get("password"));
                System.out.println(hashed);
                if(item.get("password").equals(hashed)) {
                    validPass = true;
                }
                break;
            }
            System.out.println(count);
            if(!validPass) {
                return new LoginResponse("incorrect password");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new LoginResponse("Couldn't access table");
        }

        GetUserDataRequest getUserDataRequest = new GetUserDataRequest(request.getUsername());
        GetUserDataResponse getUserDataResponse = getUserFromAlias(getUserDataRequest);
        AuthTokenDAO authTokenDAO = new AuthTokenDAO();
        User user = getUserDataResponse.getUser();
        authTokenDAO.addToken(new AuthToken(user));
        return new LoginResponse(user, new AuthToken(user));
    }

    private static String hashPassword(String passwordToHash) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(passwordToHash.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "FAILED TO HASH";
    }

    public LoginResponse register(RegisterRequest request) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion("us-west-2")
                .build();

        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable("user");

        HashMap<String, String> nameMap = new HashMap<>();
        nameMap.put("#a", "alias");
        HashMap<String, Object> valueMap = new HashMap<>();
        valueMap.put(":alias", request.getUsername());

        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("#a = :alias")
                .withNameMap(nameMap)
                .withValueMap(valueMap)
                .withScanIndexForward(false)
                .withMaxPageSize(1);

        ItemCollection<QueryOutcome> items = null;
        //List<User> users = new ArrayList<>();

        try {
            items = table.query(querySpec);
            Iterator<Item> iterator = items.iterator();
            Item item;
            boolean unique = true;
            while(iterator.hasNext()) {
                item = iterator.next();
                unique = false;
            }
            if(!unique) {
                return new LoginResponse("Username already taken");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new LoginResponse("Couldn't access table");
        }

        PutItemOutcome outcome;
        //String imageBytes = Base64.getEncoder().encodeToString(request.getImageBytes());
        String fileName = request.getUsername() + "_image.png";
        AmazonS3Client s3 = (AmazonS3Client)AmazonS3ClientBuilder
                .standard()
                .withRegion("us-west-2")
                .build();

        String imageUrl;
        try {
            byte[] encoded = Base64.getEncoder().encode(request.getImageBytes());
            PutObjectRequest putObjectRequest = new PutObjectRequest("cs340images", fileName, new ByteArrayInputStream(encoded), new ObjectMetadata())
                    .withCannedAcl(CannedAccessControlList.PublicRead);
            s3.putObject(putObjectRequest);

            imageUrl = s3.getResourceUrl("cs340images", fileName);
        } catch (AmazonServiceException e) {
            return new LoginResponse("Couldn't upload image to s3");
        }

        try {
            outcome = table
                    .putItem(new Item().withPrimaryKey("alias", request.getUsername())
                            .withString("firstName", request.getFirstName())
                            .withString("lastName", request.getLastName())
                            .withString("password", request.getPassword())
                            .withString("imageUrl", imageUrl));
        } catch (Exception e) {
            return new LoginResponse("Couldn't insert user into table");
        }

        if(outcome == null) {
            return new LoginResponse("Register failed");
        }


//        User user = new User(request.getFirstName(), request.getLastName(), request.getUsername(),
//                MALE_IMAGE_URL);
//        //user.setImageBytes(request.getImageBytes());
//        return new LoginResponse(user, new AuthToken());
        LoginRequest loginRequest = new LoginRequest(request.getUsername(), request.getPassword());
        return login(loginRequest);
    }


    public GetUserDataResponse getUserFromAlias(GetUserDataRequest request) {
//        for(User user : allFollowees) {
//            if(user.getAlias().equals(getUserDataRequest.getAlias())) {
//                return new GetUserDataResponse(user);
//            }
//        }
//        if(testUser.getAlias().equals(getUserDataRequest.getAlias())) {
//            return new GetUserDataResponse(testUser);
//        }
//        return new GetUserDataResponse(false, null);
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion("us-west-2")
                .build();

        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable("user");

        HashMap<String, String> nameMap = new HashMap<>();
        nameMap.put("#a", "alias");
        HashMap<String, Object> valueMap = new HashMap<>();
        valueMap.put(":alias", request.getAlias());

        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("#a = :alias")
                .withNameMap(nameMap)
                .withValueMap(valueMap)
                .withScanIndexForward(false)
                .withMaxPageSize(1);

        ItemCollection<QueryOutcome> items = null;

        try {
            items = table.query(querySpec);
            Iterator<Item> iterator = items.iterator();
            Item item;
            while(iterator.hasNext()) {
                item = iterator.next();
                return new GetUserDataResponse(new User((String)item.get("firstName"), (String)item.get("lastName"), (String)item.get("alias"), (String)item.get("imageUrl")));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new GetUserDataResponse("Couldn't access table");
        }

        return new GetUserDataResponse("Error getting user data");
    }
}