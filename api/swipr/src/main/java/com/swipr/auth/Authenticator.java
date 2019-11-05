package com.swipr.auth;

import java.util.Map;

import com.swipr.models.User;
import com.swipr.repository.UserRepository;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.web.client.RestTemplate;

public abstract class Authenticator {

    protected UserRepository userRepository;

    private String infoUri;
    private OAuth2AuthorizedClient client;

    public Authenticator(UserRepository userRepository, String infoUri, OAuth2AuthorizedClient client) {
        this.infoUri = infoUri;
        this.client = client;
        this.userRepository = userRepository;
    }

    // This serves as a template method for authentication
    public User authenticate() {
        ResponseEntity<Map> response = getResponse(infoUri, client);
        return getOrCreateUser(response);
    }

    public ResponseEntity<Map> getResponse(String infoUri, OAuth2AuthorizedClient client) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken().getTokenValue());
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        ResponseEntity<Map> response = restTemplate.exchange(infoUri, HttpMethod.GET, entity, Map.class);
        return response;
    }

    // Each social media login has a different way of authenticating
    public abstract User getOrCreateUser(ResponseEntity<Map> response);

}