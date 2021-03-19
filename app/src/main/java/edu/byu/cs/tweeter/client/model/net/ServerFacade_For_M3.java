package edu.byu.cs.tweeter.client.model.net;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowActionRequest;
import edu.byu.cs.tweeter.model.service.request.FollowDataRequest;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.request.TweetRequest;
import edu.byu.cs.tweeter.model.service.response.FollowActionResponse;
import edu.byu.cs.tweeter.model.service.response.FollowDataResponse;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.model.service.response.FollowingResponse;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.model.service.response.TweetResponse;

/**
 * Acts as a Facade to the Tweeter server. All network requests to the server should go through
 * this class.
 */
public class ServerFacade_For_M3 {

    // TODO: Set this to the invoke URL of your API. Find it by going to your API in AWS, clicking
    //  on stages in the right-side menu, and clicking on the stage you deployed your API to.
    private static final String SERVER_URL = "https://7m3cme2a7d.execute-api.us-west-2.amazonaws.com/dev";

    private final ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);

    /**
     * Performs a login and if successful, returns the logged in user and an auth token.
     *
     * @param request contains all information needed to perform a login.
     * @return the login response.
     */
    public LoginResponse login(LoginRequest request, String urlPath) throws IOException, TweeterRemoteException {
        LoginResponse response = clientCommunicator.doPost(urlPath, request, null, LoginResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            throw new RuntimeException(response.getMessage());
        }
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(FollowingRequest request, String urlPath)
            throws IOException, TweeterRemoteException {
        FollowingResponse response = clientCommunicator.doPost(urlPath, request, null, FollowingResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            System.out.println(response.getMessage());
            throw new RuntimeException(response.getMessage());
        }
    }
    public FollowDataResponse getFollowerData(FollowDataRequest request, String urlPath)
            throws IOException, TweeterRemoteException {
        FollowDataResponse response = clientCommunicator.doPost(urlPath, request, null, FollowDataResponse.class);
        return response;
    }

    public FollowerResponse getFollowers(FollowerRequest request, String urlPath)
            throws IOException, TweeterRemoteException {
        FollowerResponse response = clientCommunicator.doPost(urlPath, request, null, FollowerResponse.class);

        if(response.isSuccess()) {
            return response;
        } else {
            System.out.println(response.getMessage());
            throw new RuntimeException(response.getMessage());
        }
    }

    public FollowActionResponse followUser(FollowActionRequest request, String urlPath) throws IOException, TweeterRemoteException {
        FollowActionResponse response = clientCommunicator.doPost(urlPath, request, null, FollowActionResponse.class);
        return response;

    }
    public FollowActionResponse unFollowUser(FollowActionRequest request, String urlPath) throws IOException, TweeterRemoteException {
        FollowActionResponse response = clientCommunicator.doPost(urlPath, request, null, FollowActionResponse.class);
        return response;

    }
    public FollowActionResponse isFollowing(FollowActionRequest request, String urlPath) throws IOException, TweeterRemoteException {
        FollowActionResponse response = clientCommunicator.doPost(urlPath, request, null, FollowActionResponse.class);
        return response;

    }

    public TweetResponse postTweet(TweetRequest request, String urlPath) throws IOException, TweeterRemoteException{
        // need to fix this so that it acctually uses the current user
        TweetResponse response = clientCommunicator.doPost(urlPath, request, null, TweetResponse.class);
        return response;

    }
}