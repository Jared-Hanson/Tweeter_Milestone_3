package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import javax.xml.ws.RequestWrapper;

import edu.byu.cs.tweeter.model.service.request.FollowDataRequest;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.response.FollowDataResponse;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.server.service.FollowDataServiceImpl;
import edu.byu.cs.tweeter.server.service.FollowerServiceImpl;

public class GetFollowDataHandler implements RequestHandler<FollowDataRequest, FollowDataResponse> {
    @Override
    public FollowDataResponse handleRequest(FollowDataRequest request, Context context) {
        FollowDataServiceImpl service = new FollowDataServiceImpl();
        return service.getFollowerData(request);
    }
}
