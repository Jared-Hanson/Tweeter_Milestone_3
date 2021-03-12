package edu.byu.cs.tweeter.client.presenter.Observers;

import edu.byu.cs.tweeter.client.view.main.Login.RegisterSubject;

public interface RegisterObserver {
    void Update(RegisterSubject subject, String first, String last, String username, String password, boolean hasPicture);
}
