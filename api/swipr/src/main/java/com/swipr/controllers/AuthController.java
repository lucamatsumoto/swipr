package com.swipr.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.swipr.auth.GoogleTokenVerifier;
import com.swipr.models.User;
import com.swipr.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;;

@RestController
@Api(value = "AuthController", description = "Operations for authenticating users")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    private GoogleTokenVerifier tokenVerifier;
   
    @ApiOperation(value = "Verify that the token received from Google is valid and save the user in DB", response = ResponseEntity.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully authenticated the user", response = User.class),
        @ApiResponse(code = 401, message = "Invalid token received, unauthorized", response = String.class)
    })
    @RequestMapping(value="/token", method=RequestMethod.POST)
    public ResponseEntity<?> authenticateToken(@RequestBody String idToken) {
        try {
            Payload payload = tokenVerifier.VerifyToken(idToken);
            User user = new User((String) payload.get("name"), (String) payload.get("family_name"), payload.getEmail());
            userRepository.save(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        catch (Exception e) {
            // return an error object if something is invalid in the token
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

}