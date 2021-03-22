package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade_For_M3;
import edu.byu.cs.tweeter.client.util.ByteArrayUtils;
import edu.byu.cs.tweeter.model.domain.Tweet;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.StoryService_I;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;

public class StoryServiceProxy implements StoryService_I {
    static final String URL_PATH = "/story";

    @Override
    public StoryResponse getStory(StoryRequest request) throws IOException, TweeterRemoteException {
        StoryResponse response = getServerFacade().getStory(request, URL_PATH);


        if(response.isSuccess()) {
            loadImages(response);
        }

        return response;
    }

    private void loadImages(StoryResponse response) throws IOException {
        for(Tweet tweet : response.getTweets()) {
            byte [] bytes = ByteArrayUtils.bytesFromUrl(tweet.getAuthor().getImageUrl());
            tweet.getAuthor().setImageBytes(bytes);
        }
    }

    public ServerFacade_For_M3 getServerFacade() { return new ServerFacade_For_M3(); }
}
