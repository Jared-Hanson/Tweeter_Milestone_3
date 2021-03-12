package edu.byu.cs.tweeter.client.model.service;


import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.Tweet;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;
import edu.byu.cs.tweeter.client.util.ByteArrayUtils;


public class FeedService {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public StoryResponse getFeed(StoryRequest request) throws IOException {
        StoryResponse response = getServerFacade().getFeed(request);

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
    @RequiresApi(api = Build.VERSION_CODES.O)
    public ServerFacade getServerFacade() {
        return new ServerFacade();
    }
    //public void addObserver
}
