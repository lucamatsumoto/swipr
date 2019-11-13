package com.swipr.auth;

import com.swipr.repository.UserRepository;

public abstract class Authenticator {
    
    protected UserRepository userRepository;
    protected String idToken;
 
    public Authenticator(String idToken) {
        this.idToken = idToken;
    }

    public abstract boolean authenticate();
}