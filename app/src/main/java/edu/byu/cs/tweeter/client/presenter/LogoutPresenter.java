package edu.byu.cs.tweeter.client.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.LogoutServiceProxy;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.LogoutService_I;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;

public class LogoutPresenter {


    private edu.byu.cs.tweeter.client.presenter.LogoutPresenter.View view = null;

    public interface View{
        // this is where we need to add functions to update the view if there are more tweets added or more followers added.
    }
    /**
     * Creates an instance.
     *
     * @param view the view for which this class is the presenter.
     */
    public LogoutPresenter(View view) {
        this.view = view;
    }

    public LogoutPresenter() {

    }

    /**
     * Makes a logout request.
     *
     * @param logoutRequest the request.
     */
    public LogoutResponse logout(LogoutRequest logoutRequest) throws IOException, TweeterRemoteException {
        LogoutService_I logoutService = getLogoutService();
        return logoutService.logout(logoutRequest);
    }

    public LogoutService_I getLogoutService() {return new LogoutServiceProxy();}
}
