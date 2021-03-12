package edu.byu.cs.tweeter.model.service.response;

public class TweetResponse extends Response {
    public TweetResponse(boolean success) {
        super(success);
    }

    public TweetResponse(boolean success, String message) {
        super(success, message);
    }

}
