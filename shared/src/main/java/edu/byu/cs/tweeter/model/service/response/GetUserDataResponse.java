package edu.byu.cs.tweeter.model.service.response;

import edu.byu.cs.tweeter.model.domain.User;

public class GetUserDataResponse extends Response {
    private User user;

    /**
     * Creates a response indicating that the corresponding request was successful.
     *
     * @param user the user to be returned.
     */
    public GetUserDataResponse(User user) {
        super(true, null);
        this.user = user;
    }

    public GetUserDataResponse(boolean success, User user) {
        super(success);
        this.user = user;
    }

    public GetUserDataResponse(String message) {
        super(false, message);
    }

    public User getUser() {
        return user;
    }

}
