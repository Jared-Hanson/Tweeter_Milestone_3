package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.PostToFeedRequest;
import edu.byu.cs.tweeter.model.service.request.TweetRequest;
import edu.byu.cs.tweeter.server.service.PostToFeedFollowersServiceImpl;

public class PostToFeedFollowersHandler implements RequestHandler<SQSEvent, String> {

    @Override
    public String handleRequest(SQSEvent event, Context context) {
        for (SQSEvent.SQSMessage message : event.getRecords()) {
            Gson gson = new Gson();
            TweetRequest request = gson.fromJson(message.getBody(), TweetRequest.class);
            PostToFeedFollowersServiceImpl postToFeedFollowersService = new PostToFeedFollowersServiceImpl();
            try {
                postToFeedFollowersService.postTweet(request);
            } catch (IOException | TweeterRemoteException e) {
                return e.getMessage();
            }
        }
        return null;
    }
}
