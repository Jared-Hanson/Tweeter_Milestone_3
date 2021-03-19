package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.service.request.TweetRequest;
import edu.byu.cs.tweeter.model.service.response.TweetResponse;
import edu.byu.cs.tweeter.server.service.TweetServiceImpl;

/**
 * An AWS lambda function that posts a tweet
 */
public class TweetHandler implements RequestHandler<TweetRequest, TweetResponse> {
    @Override
    public TweetResponse handleRequest(TweetRequest tweetRequest, Context context) {
        TweetServiceImpl tweetService = new TweetServiceImpl();
        return tweetService.postTweet(tweetRequest);
    }
}
