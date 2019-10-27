package com.swipr.auth;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;


// Singleton Token Verifier class
public class GoogleTokenVerifier {

    private final GoogleIdTokenVerifier verifier;
    private final JsonFactory jsonFactory;
    private final NetHttpTransport transport;

    private volatile static GoogleTokenVerifier tokenVerifier;

    
    private GoogleTokenVerifier() {
        this.transport = new NetHttpTransport();
        this.jsonFactory = new JacksonFactory();
        this.verifier = new GoogleIdTokenVerifier.Builder(this.transport, this.jsonFactory).build();
    }

    public Payload VerifyToken(String idToken) throws Exception {
        try {
            // GoogleIdToken token = GoogleIdToken.parse(jsonFactory, idToken)
            GoogleIdToken token = this.verifier.verify(idToken);
            Payload payload = token.getPayload();
            if (token == null || payload == null) {
                throw new Exception("Token is invalid");
            }
            if (!payload.getEmailVerified()) {
                throw new Exception("User email is not verified");
            } 
            return payload;

        } catch(GeneralSecurityException | IOException e) {
            // Change this to logger object
            throw new Exception("Found exception " + e.getMessage());
        }
    }

    public static GoogleTokenVerifier getInstance() {
        if (tokenVerifier == null) {
            synchronized (GoogleTokenVerifier.class) {
                if (tokenVerifier == null) {
                    tokenVerifier = new GoogleTokenVerifier();
                }
            }
        }
        return tokenVerifier;
    }

}