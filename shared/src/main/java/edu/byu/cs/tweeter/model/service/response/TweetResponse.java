package edu.byu.cs.tweeter.model.service.response;

public class TweetResponse extends Response {
    public TweetResponse(boolean success) {
        super(success);
    }

    public TweetResponse(boolean success, String message) {
        super(success, message);
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof TweetResponse) {
            if(this.getMessage() == null && ((TweetResponse) o).getMessage() == null) {
                if (((TweetResponse) o).isSuccess() == this.isSuccess()) {
                    return true;
                }
            }
        }
        return false;
    }
}
