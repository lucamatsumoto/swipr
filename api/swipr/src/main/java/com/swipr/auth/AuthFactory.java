package com.swipr.auth;

import javax.security.auth.message.AuthException;

public class AuthFactory {

    public static Authenticator getAuthenticator(String authType, String idToken) throws AuthException {
        if (authType.equals("facebook")) {
            return new FacebookAuthenticator(idToken);
        }   
        else if(authType.equals("google")) {
            return new GoogleAuthenticator(idToken);
        } else {
            throw new AuthException("Auth type is invalid");
        }
    }
}