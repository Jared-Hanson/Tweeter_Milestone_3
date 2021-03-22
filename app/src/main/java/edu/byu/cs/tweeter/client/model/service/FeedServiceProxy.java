package edu.byu.cs.tweeter.client.model.service;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade_For_M3;
import edu.byu.cs.tweeter.client.util.ByteArrayUtils;
import edu.byu.cs.tweeter.model.domain.Tweet;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.FeedService_I;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;

public class FeedServiceProxy implements FeedService_I {
    static final String URL_PATH = "/feed";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public StoryResponse getFeed(StoryRequest request) throws IOException, TweeterRemoteException {
        StoryResponse response = getServerFacade().getFeed(request, URL_PATH);

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
    public ServerFacade_For_M3 getServerFacade() {
        return new ServerFacade_For_M3();
    }
}
