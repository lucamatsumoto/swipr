package com.swipr.auth;

import java.util.Map;

import com.swipr.models.User;
import com.swipr.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;

public class FacebookAuthenticator extends Authenticator {

    public FacebookAuthenticator(UserRepository userRepository, String infoUri, OAuth2AuthorizedClient client) {
        super(userRepository, infoUri, client);
    }

    @Override
    public User getOrCreateUser(ResponseEntity<Map> response) {
        User user = null;
        System.out.println(response.getBody());
        Object firstName = response.getBody().get("first_name");
        Object lastName = response.getBody().get("last_name");
        Object email = response.getBody().get("email");
        if (userRepository.findByEmail(email.toString()).isEmpty()) {
            user = new User(firstName.toString(), lastName.toString(), email.toString());
            userRepository.save(user);
        } else {
            user = userRepository.findByEmail(email.toString()).get(0);
        }
        return user;
    }
}