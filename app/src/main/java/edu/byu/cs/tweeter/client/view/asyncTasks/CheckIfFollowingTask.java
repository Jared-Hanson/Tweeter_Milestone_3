package edu.byu.cs.tweeter.client.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.request.FollowActionRequest;
import edu.byu.cs.tweeter.model.service.response.FollowActionResponse;
import edu.byu.cs.tweeter.client.presenter.Follow_UnfollowActionPresenter;

public class CheckIfFollowingTask extends AsyncTask<FollowActionRequest, Void, FollowActionResponse> {

    private final Follow_UnfollowActionPresenter presenter;
    private final Observer observer;
    private Exception exception;



    public interface Observer {
        void run(FollowActionResponse followerResponse);
        void handleException(Exception exception);
    }


    public CheckIfFollowingTask(Follow_UnfollowActionPresenter presenter, Observer observer) {
        if(observer == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }


    @Override
    protected FollowActionResponse doInBackground(FollowActionRequest... followerRequests) {

        FollowActionResponse response = null;

        try {
            response = presenter.isFollowing(followerRequests[0]);
        } catch (IOException ex) {
            exception = ex;
        }

        return response;
    }


    @Override
    protected void onPostExecute(FollowActionResponse followerResponse) {
        if(exception != null) {
            observer.handleException(exception);
        } else {
            observer.run(followerResponse);
        }
    }
}