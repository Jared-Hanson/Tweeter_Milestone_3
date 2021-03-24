package edu.byu.cs.tweeter.server.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.GetUserDataService_I;
import edu.byu.cs.tweeter.model.service.request.GetUserDataRequest;
import edu.byu.cs.tweeter.model.service.response.GetUserDataResponse;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class GetUserDataServiceImpl implements GetUserDataService_I {
    @Override
    public GetUserDataResponse getUserData(GetUserDataRequest getUserDataRequest) throws IOException, TweeterRemoteException {
        UserDAO userDAO = getUserDAO();
        return userDAO.getUserFromAlias(getUserDataRequest);
    }

    public UserDAO getUserDAO() {
        return new UserDAO();
    }
}
