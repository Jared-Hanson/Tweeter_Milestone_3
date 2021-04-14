package edu.byu.cs.tweeter.server;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.FollowingDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class FillScript {
    private final static int NUM_USERS = 10000;

    // The alias of the user to be followed by each user created
    // This example code does not add the target user, that user must be added separately.
    private final static String FOLLOW_TARGET = "@jrod";

    public static void main(String args[]) {

        // Get instance of DAOs by way of the Abstract Factory Pattern
        //UserDAO userDAO = new UserDAO();
        FollowingDAO followDAO = new FollowingDAO();

        List<String> followers = new ArrayList<>();
        //List<User> users = new ArrayList<>();

        // Iterate over the number of users you will create
        for (int i = 1; i <= NUM_USERS; i++) {

            //String name = "Guy";
            String alias = "@guy" + i;

            /*
            // Note that in this example, a UserDTO only has a name and an alias.
            // The url for the profile image can be derived from the alias in this example
            User user = new User();
            user.setAlias(alias);
            user.setFirstName(name);
            user.setLastName("" + i);
            user.setImageUrl("https://cs340images.s3.us-west-2.amazonaws.com/test_image.png");
            users.add(user);


             */
            // Note that in this example, to represent a follows relationship, only the aliases
            // of the two users are needed
            followers.add(alias);
        }

        // Call the DAOs for the database logic
        /*
        if (users.size() > 0) {
            userDAO.addUserBatch(users);
        }
         */
        if (followers.size() > 0) {
            followDAO.addFollowersBatch(followers, FOLLOW_TARGET);
        }
    }
}
