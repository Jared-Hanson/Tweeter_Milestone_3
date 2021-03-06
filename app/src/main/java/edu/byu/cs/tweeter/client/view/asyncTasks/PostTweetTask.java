package edu.byu.cs.tweeter.client.view.asyncTasks;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.TweetRequest;
import edu.byu.cs.tweeter.model.service.response.TweetResponse;
import edu.byu.cs.tweeter.client.presenter.TweetPresenter;

public class PostTweetTask extends AsyncTask<TweetRequest, Void, TweetResponse> {
    private final TweetPresenter presenter;
    private final Observer observer;
    private Exception exception;

    public interface Observer {
        void postTweetResponse(TweetResponse tweetResponse);
        void handleException(Exception exception);
    }

    public PostTweetTask(TweetPresenter presenter, Observer observer) {

        if(observer == null) {
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected TweetResponse doInBackground(TweetRequest... tweetRequests) {

        TweetResponse response = null;

        try {
            response = presenter.postTweet(tweetRequests[0]);
        } catch (IOException | TweeterRemoteException ex) {
            exception = ex;
        }

        return response;
    }

    @Override
    protected void onPostExecute(TweetResponse tweetResponse) {
        if(exception != null) {
            observer.handleException(exception);
        } else {
            observer.postTweetResponse(tweetResponse);
        }
    }

}
