package edu.byu.cs.tweeter.model.service.request;

import java.util.Objects;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowActionRequest {
    private User user;
    private User userToFollow;
    private AuthToken authToken;

    public FollowActionRequest(User user, User userToFollow) {
        this.user = user;
        this.userToFollow = userToFollow;
    }

    public FollowActionRequest(User user, User userToFollow, AuthToken authToken) {
        this.user = user;
        this.userToFollow = userToFollow;
        this.authToken = authToken;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public FollowActionRequest() {
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUserToFollow(User userToFollow) {
        this.userToFollow = userToFollow;
    }

    public User getUser() {
        return user;
    }

    public User getUserToFollow() {
        return userToFollow;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FollowActionRequest)) return false;
        FollowActionRequest that = (FollowActionRequest) o;
        return Objects.equals(getUser(), that.getUser()) &&
                Objects.equals(getUserToFollow(), that.getUserToFollow());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser(), getUserToFollow());
    }
}
