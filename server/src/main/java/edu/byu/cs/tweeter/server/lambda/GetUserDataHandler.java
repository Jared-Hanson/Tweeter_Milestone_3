package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.GetUserDataService_I;
import edu.byu.cs.tweeter.model.service.request.GetUserDataRequest;
import edu.byu.cs.tweeter.model.service.response.GetUserDataResponse;
import edu.byu.cs.tweeter.server.service.GetUserDataServiceImpl;

public class GetUserDataHandler implements RequestHandler<GetUserDataRequest, GetUserDataResponse> {
    @Override
    public GetUserDataResponse handleRequest(GetUserDataRequest getUserDataRequest, Context context) {
        GetUserDataService_I getUserDataService = new GetUserDataServiceImpl();
        try {
            return getUserDataService.getUserData(getUserDataRequest);
        } catch (IOException | TweeterRemoteException e) {
            return new GetUserDataResponse("Error retrieving user");
        }
    }
}
