package edu.byu.cs.tweeter.client.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.TweetService;
import edu.byu.cs.tweeter.model.service.request.TweetRequest;
import edu.byu.cs.tweeter.model.service.response.TweetResponse;

public class TweetPresenter {
    private edu.byu.cs.tweeter.client.presenter.TweetPresenter.View view = null;

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
    public TweetPresenter(edu.byu.cs.tweeter.client.presenter.TweetPresenter.View view) {
        this.view = view;
    }
    public TweetPresenter() {

    }

    /**
     * Makes a login request.
     *
     * @param request the request.
     */
    public TweetResponse postTweet(TweetRequest request) throws IOException {
        TweetService tweetService = getTweetService();
        return tweetService.postTweet(request);
    }

    public TweetService getTweetService() {return new TweetService();}
}