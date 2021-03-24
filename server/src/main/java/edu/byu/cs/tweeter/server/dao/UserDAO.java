package edu.byu.cs.tweeter.server.dao;

public class LoginDAO {
    public LoginResponse login(LoginRequest request) {


        Log.d("info", "login: " + request.getUsername());
        Log.d("info", "login: " + request.getPassword());
        if (request.getUsername().equals("dummyUserName") && request.getPassword().equals("dummyPassword")) {
            Log.d("info", "logged in?");
            return new LoginResponse(testUser, new AuthToken());
        }
        // this is here becuase i got tired of typing that long user name in each time
        else if (request.getUsername().equals("d") && request.getPassword().equals("dddddddd")) {
            Log.d("info", "logged in?");
            return new LoginResponse(testUser, new AuthToken());
        }
        else {
            return new LoginResponse("Invalid credentials");
        }

    }
}