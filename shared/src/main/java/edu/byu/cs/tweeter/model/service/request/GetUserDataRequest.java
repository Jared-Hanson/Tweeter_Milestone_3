package edu.byu.cs.tweeter.model.service.request;

public class GetUserDataRequest {
    private String alias;

    public GetUserDataRequest(String alias) {
        this.alias = alias;
    }

    public GetUserDataRequest() {}

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }
}
