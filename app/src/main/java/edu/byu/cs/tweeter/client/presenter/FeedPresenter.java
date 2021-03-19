package edu.byu.cs.tweeter.client.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.FeedService;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;

public class FeedPresenter {
    private final edu.byu.cs.tweeter.client.presenter.FeedPresenter.View view;

    public interface View{
        // this is where we need to add functions to update the view if there are more tweets added or more followers added.

        //void updateFeed();
    }

    public FeedPresenter(edu.byu.cs.tweeter.client.presenter.FeedPresenter.View view) {
        this.view = view;
        //FeedService feed = getFeedService();
        //feed.addObserver(this);
    }

    public StoryResponse getFeed(StoryRequest request) throws IOException {
        FeedService feedService = getFeedService();
        return feedService.getFeed(request);
    }

    public FeedService getFeedService() {return new FeedService();}
}