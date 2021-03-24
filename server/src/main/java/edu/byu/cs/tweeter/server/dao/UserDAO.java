package edu.byu.cs.tweeter.server.dao;

import java.util.Arrays;
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
        if (request.getUsername().equals("dummyUserName") && request.getPassword().equals("dummyPassword")) {
            return new LoginResponse(testUser, new AuthToken());
        }
        // this is here becuase i got tired of typing that long user name in each time
        else if (request.getUsername().equals("d") && request.getPassword().equals("dddddddd")) {
            return new LoginResponse(testUser, new AuthToken());
        }
        else {
            return new LoginResponse("Invalid credentials");
        }

    }

    public LoginResponse register(RegisterRequest request) {
        if (request.getUsername().equals(testUser.getAlias()) ||
                request.getUsername().equals(user1.getAlias())||
                request.getUsername().equals(user2.getAlias())||
                request.getUsername().equals(user3.getAlias())||
                request.getUsername().equals(user4.getAlias())||
                request.getUsername().equals(user5.getAlias())||
                request.getUsername().equals(user6.getAlias())||
                request.getUsername().equals(user7.getAlias())||
                request.getUsername().equals(user8.getAlias())||
                request.getUsername().equals(user9.getAlias())||
                request.getUsername().equals(user10.getAlias())||
                request.getUsername().equals(user11.getAlias())||
                request.getUsername().equals(user12.getAlias())||
                request.getUsername().equals(user13.getAlias())||
                request.getUsername().equals(user14.getAlias())||
                request.getUsername().equals(user15.getAlias())||
                request.getUsername().equals(user16.getAlias())||
                request.getUsername().equals(user17.getAlias())||
                request.getUsername().equals(user18.getAlias())||
                request.getUsername().equals(user19.getAlias())||
                request.getUsername().equals(user20.getAlias())) {
            return new LoginResponse("Username already taken");
        }
        User user = new User(request.getFirstName(), request.getLastName(), request.getUsername(),
                null);
        user.setImageBytes(request.getImageBytes());
        return new LoginResponse(user, new AuthToken());
    }

    public LogoutResponse logout(LogoutRequest logoutRequest) {
        LogoutResponse logoutResponse = null;
        if(logoutRequest.getUser() != null && logoutRequest.getAuthToken() != null) {
            logoutResponse = new LogoutResponse(true, logoutRequest.getUser() + " succesfully logged out");
        }

        return logoutResponse;
    }

    public GetUserDataResponse getUserFromAlias(GetUserDataRequest getUserDataRequest) {
        for(User user : allFollowees) {
            if(user.getAlias().equals(getUserDataRequest.getAlias())) {
                return new GetUserDataResponse(user);
            }
        }
        return new GetUserDataResponse(false, null);
    }
}