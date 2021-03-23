package edu.byu.cs.tweeter.model.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowActionServiceProxy;
import edu.byu.cs.tweeter.client.model.service.FollowerServiceProxy;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowActionRequest;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.response.FollowActionResponse;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;

public class Integration_FollowActionTest {

    private FollowActionRequest validRequest;


    private FollowActionResponse successResponseFollow;
    private FollowActionResponse successResponseUnFollow;
    private FollowActionResponse successResponseIsFollow;



    private FollowActionServiceProxy followingServiceSpy;






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

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        User currentUser = new User("FirstName", "LastName",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        // Setup request objects to use in the tests
        validRequest = new FollowActionRequest(user2, user1);


        // Setup a mock ServerFacade that will return known responses
        successResponseFollow = followUser(validRequest);
        successResponseUnFollow = unFollowUser(validRequest);
        successResponseIsFollow = new FollowActionResponse(true);



        // Create a FollowingService instance and wrap it with a spy that will use the mock service
        followingServiceSpy = Mockito.spy(new FollowActionServiceProxy());

    }

    @Test
    public void testFollow_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FollowActionResponse response = followingServiceSpy.followUser(validRequest);
        Assertions.assertEquals(successResponseFollow.getMessage(), response.getMessage());
    }
    @Test
    public void testUnFollow_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FollowActionResponse response = followingServiceSpy.unFollowUser(validRequest);
        Assertions.assertEquals(successResponseUnFollow.getMessage(), response.getMessage());
    }
    @Test
    public void testIsFollow_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        FollowActionResponse response = followingServiceSpy.isFollowing(validRequest);
        System.out.println(response.isSuccess());
        Assertions.assertEquals(successResponseIsFollow.isSuccess(), response.isSuccess());
    }





    public FollowActionResponse followUser(FollowActionRequest request){
        FollowActionResponse response = new FollowActionResponse(true, "User " + request.getUser().getAlias() + " is now following " + request.getUserToFollow().getAlias());
        return response;
    }
    public FollowActionResponse unFollowUser(FollowActionRequest request){
        FollowActionResponse response = new FollowActionResponse(true, "User @" + request.getUser().getAlias() + " is no longer following @" + request.getUserToFollow().getAlias());
        return response;
    }
    public FollowActionResponse isFollowing(FollowActionRequest request){
        FollowActionResponse response;
        if(loginFollowees.contains(request.getUserToFollow())) {
            response = new FollowActionResponse(true);
        }
        else{
            response = new FollowActionResponse(false);
        }
        return response;
    }

}
