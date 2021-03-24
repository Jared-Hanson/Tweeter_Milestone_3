package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.RegisterService_I;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.server.service.RegisterServiceImpl;

/**
 * An AWS lambda function that registers a user and returns the user object and an auth code for
 * a successful login.
 */
public class RegisterHandler implements RequestHandler<RegisterRequest, LoginResponse> {
    @Override
    public LoginResponse handleRequest(RegisterRequest registerRequest, Context context) {
        RegisterService_I registerService = new RegisterServiceImpl();
        try {
            return registerService.register(registerRequest);
        } catch (IOException | TweeterRemoteException e) {
            return new LoginResponse("Error registering user");
        }
    }
}