package edu.byu.cs.tweeter.client.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.RegisterServiceProxy;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.RegisterService_I;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.client.presenter.Observers.RegisterObserver;
import edu.byu.cs.tweeter.client.view.main.Login.RegisterFragment;
import edu.byu.cs.tweeter.client.view.main.Login.RegisterSubject;

/**
 * The presenter for the register functionality of the application.
 */
public class RegisterPresenter implements RegisterObserver {

    private final View view;

    @Override
    public void Update(RegisterSubject subject, String first, String last, String username, String password, boolean hasPicture) {
        if(subject instanceof RegisterFragment) {
            RegisterFragment registerFragment = (RegisterFragment) subject;
            if(first.length() > 0 && last.length() > 0 && username.length() > 0 && password.length() >= 6 && hasPicture) {
                registerFragment.setButton(true);
            } else {
                registerFragment.setButton(false);
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
    public RegisterPresenter(View view) {
        this.view = view;
    }

    /**
     * Makes a register request.
     *
     * @param registerRequest the request.
     */
    public LoginResponse register(RegisterRequest registerRequest) throws IOException, TweeterRemoteException {
        RegisterService_I registerService = getRegisterService();
        return registerService.register(registerRequest);
    }

    public RegisterService_I getRegisterService() {return new RegisterServiceProxy();}
}
