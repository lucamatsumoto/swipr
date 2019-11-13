package com.swipr.auth;

public class GoogleAuthenticator extends Authenticator {

    public GoogleAuthenticator(String idToken) {
        super(idToken);
    }

    @Override
    public boolean authenticate() {
        return true;
    }
}