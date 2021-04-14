package edu.byu.cs.tweeter.client.model.service;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.math.BigDecimal;

import edu.byu.cs.tweeter.client.model.net.ServerFacade_For_M3;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.TweetService_I;
import edu.byu.cs.tweeter.model.service.request.TweetRequest;
import edu.byu.cs.tweeter.model.service.response.TweetResponse;

public class TweetServiceProxy implements TweetService_I {
    static final String URL_PATH = "/posttweet";


    @RequiresApi(api = Build.VERSION_CODES.O)
    public TweetResponse postTweet(TweetRequest request) throws IOException, TweeterRemoteException {
        TweetResponse response = getServerFacade().postTweet(request, URL_PATH);
        return response;
    }

    public ServerFacade_For_M3 getServerFacade() {
        return new ServerFacade_For_M3();
    }
}
