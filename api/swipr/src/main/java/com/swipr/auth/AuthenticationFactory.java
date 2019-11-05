package com.swipr.auth;

import com.swipr.repository.UserRepository;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;

public class AuthenticationFactory {

    public static Authenticator getAuthenticator(UserRepository userRepository, String infoUri, OAuth2AuthorizedClient client) {
        if (infoUri.contains("facebook")) {
            return new FacebookAuthenticator(userRepository, infoUri, client);
        }
        else if(infoUri.contains("google")) {
            return new GoogleAuthenticator(userRepository, infoUri, client);
        }
        else {
            return null;
        }
    }

}