package edu.byu.cs.tweeter.server.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.LoginService_I;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class LoginServiceImpl implements LoginService_I {

    @Override
    public LoginResponse login(LoginRequest request) throws IOException, TweeterRemoteException {
        UserDAO userDAO = getUserDAO();
        LoginResponse response = userDAO.login(request);
        if(response.getAuthToken() != null) {
            AuthTokenDAO authTokenDAO = getAuthTokenDAO();
            authTokenDAO.addToken(response.getAuthToken());
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
