package edu.byu.cs.tweeter.client.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.LoginServiceProxy;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.LoginService_I;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.client.presenter.Observers.LoginObserver;
import edu.byu.cs.tweeter.client.view.main.Login.LoginFragment;
import edu.byu.cs.tweeter.client.view.main.Login.LoginSubject;

/**
 * The presenter for the login functionality of the application.
 */
public class LoginPresenter implements LoginObserver {

    private final View view;

    @Override
    public void Update(LoginSubject subject, String username, String password) {
        if(subject instanceof LoginFragment) {
            LoginFragment loginFragment = (LoginFragment) subject;
            if(username.length() > 0 && password.length() >= 6) {
                loginFragment.setButton(true);
            }
            else {
                loginFragment.setButton(false);
            }
        }
    }

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }

    /**
     * Creates an instance.
     *
     * @param view the view for which this class is the presenter.
     */
    public LoginPresenter(View view) {
        this.view = view;
    }

    /**
     * Makes a login request.
     *
     * @param loginRequest the request.
     */
    public LoginResponse login(LoginRequest loginRequest) throws IOException, TweeterRemoteException {
        LoginService_I loginService = getLoginService();
        return loginService.login(loginRequest);
    }

    public LoginService_I getLoginService() {return new LoginServiceProxy();};
}
