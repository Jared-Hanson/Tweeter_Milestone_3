package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.LogoutService_I;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.server.service.LogoutServiceImpl;

public class LogoutHandler implements RequestHandler<LogoutRequest, LogoutResponse> {
    @Override
    public LogoutResponse handleRequest(LogoutRequest logoutRequest, Context context) {
        LogoutService_I logoutService = new LogoutServiceImpl();
        try {
            return logoutService.logout(logoutRequest);
        } catch (IOException | TweeterRemoteException e) {
            return new LogoutResponse(false, "Error logging out");
        }
    }
}
