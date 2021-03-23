package edu.byu.cs.tweeter.client.presenter;

import java.io.IOException;


import edu.byu.cs.tweeter.client.model.service.StoryServiceProxy;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.StoryService_I;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;


public class StoryPresenter {
    private final View view;

    public interface View{
        // this is where we need to add functions to update the view if there are more tweets added or more followers added.
    }

    public StoryPresenter(View view) {this.view = view;}

    public StoryResponse getStory(StoryRequest request) throws IOException, TweeterRemoteException {
        StoryService_I storyService = getStoryService();
        return storyService.getStory(request);
    }

    public StoryServiceProxy getStoryService() {return new StoryServiceProxy();}
}
