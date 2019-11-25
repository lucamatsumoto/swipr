package com.swipr.controllers;

import java.util.List;

import com.swipr.models.User;
import com.swipr.repository.UserRepository;
import com.swipr.utils.UserSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

/**
 * Represents a Websocket handler for creating, deleting, and retrieving user information
 */

@Controller
public class UserController {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    private UserSessionManager userSessionManager = UserSessionManager.getInstance();

    /**
     * Retrieves all users that have been created. Use for debugging
     * @param headerAccessor header object that is sent with every request
     */
    @MessageMapping("/all")
    public void getUsers(SimpMessageHeaderAccessor headerAccessor) {
        try {
            List<User> allUsers = userRepository.findAll();
            userSessionManager.sendToUser(headerAccessor, "/queue/reply", allUsers, messagingTemplate);
        } catch(DataAccessException e) {
            userSessionManager.sendToUser(headerAccessor, "/queue/error", e.getLocalizedMessage(), messagingTemplate);
        }
    }

    /**
     * Creates an authenticated user's object into the database
     * @param user the authenticated user
     * @param headerAccessor header object that is sent with every request
     */
    @MessageMapping("/create") 
    public void createUser(@Payload User user, SimpMessageHeaderAccessor headerAccessor) {
        // Make sure to add error handling later as well 
        try {
            if (userRepository.findByEmail(user.getEmail()).isEmpty()) {
                userRepository.save(user);
            } 
            user = userRepository.findByEmail(user.getEmail()).get(0);
            // Keep track of a map of users to sessionId in RAM
            userSessionManager.sendToUser(headerAccessor, "/queue/reply", user, messagingTemplate);
        } catch (DataAccessException e) {
            userSessionManager.sendToUser(headerAccessor, "/queue/error", e.getLocalizedMessage(), messagingTemplate);
        }
    } 

    /**
     * Deletes a specific user from the database
     * @param user the authenticated user
     * @param headerAccessor header object that is sent with every request
     */
    @MessageMapping("/delete")
    public void deleteUser(@Payload User user, SimpMessageHeaderAccessor headerAccessor) {
        try {
            userRepository.deleteById(user.getId());
            userSessionManager.sendToUser(headerAccessor, "/queue/reply", "deleted user with email " + user.getEmail(),  messagingTemplate);
        } catch(DataAccessException e) {
            userSessionManager.sendToUser(headerAccessor, "/queue/error", e.getLocalizedMessage(),  messagingTemplate);
        }
    }

    /**
     * Updates the venmo account of a specific user
     * @param user The authenticated user that would like to update their information
     * @param headerAccessor header object that is sent with every request
     */
    @MessageMapping("/updateVenmo")
    public void updateVenmo(@Payload User user, SimpMessageHeaderAccessor headerAccessor) {
        List<User> users = userRepository.findByEmail(user.getEmail());
        if (!users.isEmpty()) {
            try {
                userRepository.updateUserByEmail(user.getVenmo(), user.getEmail());
                userSessionManager.sendToUser(headerAccessor, "/queue/reply", user,  messagingTemplate);
            } catch(DataAccessException e) {
                userSessionManager.sendToUser(headerAccessor, "/queue/error", e.getLocalizedMessage(),  messagingTemplate);
            }
        } else {
            userSessionManager.sendToUser(headerAccessor, "/queue/error", "The user you looked for doesn't exist", messagingTemplate);
        }
    }

}