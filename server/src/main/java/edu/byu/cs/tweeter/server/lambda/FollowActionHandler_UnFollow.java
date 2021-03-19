package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.service.request.FollowActionRequest;
import edu.byu.cs.tweeter.model.service.response.FollowActionResponse;
import edu.byu.cs.tweeter.server.service.FollowActionServiceImpl;

public class FollowActionHandler_UnFollow implements RequestHandler<FollowActionRequest, FollowActionResponse> {
    @Override
    public FollowActionResponse handleRequest(FollowActionRequest input, Context context) {
        FollowActionServiceImpl service = new FollowActionServiceImpl();
        return service.unFollowUser(input);
    }
}
