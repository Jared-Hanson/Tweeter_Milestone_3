package edu.byu.cs.tweeter.model.domain;

import java.io.Serializable;
import java.util.UUID;

/**
 * Represents an auth token in the system.
 */
public class AuthToken implements Serializable {
    private String token;
    private User user;

    public AuthToken(User user) {
        this.user = user;
        this.token = generateToken();
    }

    public AuthToken() {
    }

    public String getToken() {
        return token;
    }

    private String generateToken() {
        return UUID.randomUUID().toString().toUpperCase();
    }

    public User getUser() {
        return user;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
