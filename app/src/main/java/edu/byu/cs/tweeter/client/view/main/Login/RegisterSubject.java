package edu.byu.cs.tweeter.client.view.main.Login;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.presenter.Observers.RegisterObserver;

public class RegisterSubject extends Fragment {
    private List<RegisterObserver> observerList = new ArrayList<>();

    void Attach(RegisterObserver observer) {
        observerList.add(observer);
    }

    void Notify(String first, String last, String username, String password, boolean hasPicture) {
        for(RegisterObserver observer : observerList) {
            observer.Update(this, first, last, username, password, hasPicture);
        }
    }
}
