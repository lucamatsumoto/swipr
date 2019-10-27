package com.swipr.controllers;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

import javax.validation.Valid;

import com.swipr.models.User;
import com.swipr.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@Api(value = "UserController", description = "Operations pertaining to creating, updating, and getting user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Sample stuff
    @ApiOperation(value = "Test endpoint", response = ResponseEntity.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "User test working\n", response = String.class)
    })
    @RequestMapping(value="/user/test", method=RequestMethod.GET) 
    public ResponseEntity<?> testEndpoint() {
        return new ResponseEntity<>("User test working\n", HttpStatus.OK);
    }

    @ApiOperation(value = "Create a user/Check if the user exists", response = ResponseEntity.class)
    @ApiResponses(value = {
        @ApiResponse(code = 202, message = "User found", response = User.class),
        @ApiResponse(code = 201, message = "User created", response = User.class)
    })
    @RequestMapping(value="/user/create", method=RequestMethod.POST)
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        // If the user has already been created, then return the created user
        List<User> users = userRepository.findByEmail(user.getEmail());
        if (!users.isEmpty()) {
            // Assume that there will be unique emails
            return new ResponseEntity<>(users.get(0), HttpStatus.ACCEPTED);
        }
        // Otherwise create the user and save in our repository
        userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get a list of all users. Use to test if DB is working", response = ResponseEntity.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "List of users found", response = User.class)
    })
    @RequestMapping(value="/user/all", method=RequestMethod.GET)
    public ResponseEntity<?> getAllUsers() {
        // get all users and return them
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    @ApiOperation(value = "Update the Venmo Account of a user", response = ResponseEntity.class)
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Updated user", response = User.class),
        @ApiResponse(code = 400, message = "JDBC database driver found an error", response = String.class)
    })
    @RequestMapping(value="/user/update", method=RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user) {
        // search for the user's email and update their venmo account
        try {
            userRepository.updateUserByEmail(user.getVenmo(), user.getEmail());
            return new ResponseEntity<>(user, HttpStatus.ACCEPTED);   
        }
        // Something went wrong with the JDBC driver
        catch (DataAccessException e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        } 
    }

}