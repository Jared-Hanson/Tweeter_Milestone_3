package edu.byu.cs.tweeter.server.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.PostToFeedFollowersService_I;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.request.PostToFeedRequest;
import edu.byu.cs.tweeter.model.service.request.TweetRequest;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.model.service.response.TweetResponse;
import edu.byu.cs.tweeter.server.dao.FollowingDAO;

public class PostToFeedFollowersServiceImpl implements PostToFeedFollowersService_I {
    @Override
    public TweetResponse postTweet(TweetRequest request) throws IOException, TweeterRemoteException {
        FollowerRequest followerRequest = new FollowerRequest(request.getUser(), 10000, null);
        FollowingDAO followingDAO = getFollowerDAO();
        FollowerResponse response = followingDAO.getFollowers(followerRequest);
        PostToFeedRequest postToFeedRequest = new PostToFeedRequest();
        postToFeedRequest.setUser(request.getUser());
        postToFeedRequest.setTweetBody(request.getTweetBody());
        postToFeedRequest.setDate(request.getDate());
        postToFeedRequest.setAuthToken(request.getAuthToken());
        do {
            User follower = null;
            List<User> followers = response.getFollowers();
            Iterator<User> iterator = followers.iterator();
            while (iterator.hasNext()) {
                follower = iterator.next();
                postToFeedRequest.setFollower(follower.getAlias());

                Gson gson = new Gson();
                String messageBody = gson.toJson(postToFeedRequest);
                String queueUrl = "https://sqs.us-west-2.amazonaws.com/223082554131/feed_queue_post_phase";

                SendMessageRequest send_msg_request = new SendMessageRequest()
                        .withQueueUrl(queueUrl)
                        .withMessageBody(messageBody)
                        .withDelaySeconds(0);

                AmazonSQS sqs = getSQS();
                sqs.sendMessage(send_msg_request);
            }

            if (response.getHasMorePages()) {
                followerRequest.setLastFolloweeAlias(follower.getAlias());
                response = followingDAO.getFollowers(followerRequest);
            }
        } while (response.getHasMorePages());
        return null;
    }

    FollowingDAO getFollowerDAO() {
        return new FollowingDAO();
    }

    public AmazonSQS getSQS() {
        return AmazonSQSClientBuilder.defaultClient();
    }
}
