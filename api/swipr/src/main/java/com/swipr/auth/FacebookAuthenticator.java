package com.swipr.auth;

public class FacebookAuthenticator extends Authenticator {

    public FacebookAuthenticator(String idToken) {
        super(idToken);
    }

    @Override
    public boolean authenticate() {
        return true;
    }
}