package edu.byu.cs.tweeter.client.presenter.Observers;

import edu.byu.cs.tweeter.client.view.main.Login.LoginSubject;

public interface LoginObserver {
    void Update(LoginSubject subject, String username, String password);
}
