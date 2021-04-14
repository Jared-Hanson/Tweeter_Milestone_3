package edu.byu.cs.tweeter.server.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.LogoutService_I;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class LogoutServiceImpl implements LogoutService_I {
    @Override
    public LogoutResponse logout(LogoutRequest request) throws IOException, TweeterRemoteException {
        LogoutResponse response = getAuthTokenDAO().logout(request);
        if(response.isSuccess()) {
           AuthTokenDAO authTokenDAO = getAuthTokenDAO();
           authTokenDAO.removeToken(request.getAuthToken());
        }
        return response;
    }

    public UserDAO getUserDAO() {
        return new UserDAO();
    }

    public AuthTokenDAO getAuthTokenDAO() {
        return new AuthTokenDAO();
    }
}
