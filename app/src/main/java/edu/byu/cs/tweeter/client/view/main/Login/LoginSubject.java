package edu.byu.cs.tweeter.client.view.main.Login;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.presenter.Observers.LoginObserver;

public abstract class LoginSubject extends Fragment {
    private List<LoginObserver> observerList = new ArrayList<>();

    void Attach(LoginObserver observer) {
        observerList.add(observer);
    }

    void Notify(String username, String password) {
        for(LoginObserver observer : observerList) {
            observer.Update(this, username, password);
        }
    }
}
