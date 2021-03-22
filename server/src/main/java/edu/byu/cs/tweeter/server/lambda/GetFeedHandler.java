package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;
import edu.byu.cs.tweeter.server.service.FeedServiceImpl;

public class GetFeedHandler implements RequestHandler<StoryRequest, StoryResponse> {
    @Override
    public StoryResponse handleRequest(StoryRequest input, Context context) {
        FeedServiceImpl feedService = new FeedServiceImpl();
        return feedService.getFeed(input);
    }
}
