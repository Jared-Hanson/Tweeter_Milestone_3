package edu.byu.cs.tweeter.client.model.service;


import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.Tweet;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;
import edu.byu.cs.tweeter.client.util.ByteArrayUtils;


public class StoryService {


    public StoryResponse getStory(StoryRequest request) throws IOException {
        StoryResponse response = getServerFacade().getStory(request);


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



    public ServerFacade getServerFacade() { return new ServerFacade(); }
}
