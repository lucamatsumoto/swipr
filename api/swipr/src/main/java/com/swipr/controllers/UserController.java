package com.swipr.controllers;

import com.swipr.models.User;
import com.swipr.repository.UserRepository;
import com.swipr.utils.UserSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

/**
 * Represents a Websocket handler for creating, deleting, and retrieving user information
 */

@Controller
public class UserController {


    /**
     * Interface for handling database interactions for user business logic
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Object for handling networking logic of sending and receiving from users, retrieving session IDs, headers, etc. 
     */
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    /**
     * UserSessionManager object contains information about each sessions ID, headers, and other important information and their corresponding User objects  
     */ 
    private UserSessionManager userSessionManager = UserSessionManager.getInstance();

    /**
     * Retrieves all users that have been created
     * @param headerAccessor header object that is sent with every request
     */
    @MessageMapping("/all")
    @SendToUser("/queue/reply")
    public void getUsers(SimpMessageHeaderAccessor headerAccessor) {
        messagingTemplate.convertAndSendToUser(headerAccessor.getSessionId(), "/queue/reply", userRepository.findAll(), headerAccessor.getMessageHeaders());
    }

    /**
     * Creates an authenticated user's object into the database
     * @param user the authenticated user
     * @param headerAccessor header object that is sent with every request
     */
    @MessageMapping("/create") 
    @SendToUser("/queue/reply")
    public void createUser(@Payload User user, SimpMessageHeaderAccessor headerAccessor) {
        // Make sure to add error handling later as well 
        if (userRepository.findByEmail(user.getEmail()).isEmpty()) {
            userRepository.save(user);
        } 
        else {
            user = userRepository.findByEmail(user.getEmail()).get(0);
        }
        // Keep track of a map of users to sessionId in RAM
        userSessionManager.addSession(user, headerAccessor);
        messagingTemplate.convertAndSendToUser(headerAccessor.getSessionId(), "/queue/reply", user, headerAccessor.getMessageHeaders()); 
    } 

    /**
     * Deletes a specific user from the database
     * @param user the authenticated user
     * @param headerAccessor header object that is sent with every request
     */
    @MessageMapping("/delete")
    @SendToUser("/queue/reply")
    public void deleteUser(@Payload User user, SimpMessageHeaderAccessor headerAccessor) {
        userRepository.delete(user);
        messagingTemplate.convertAndSendToUser(headerAccessor.getSessionId(), "/queue/reply", "deleted user with email " + user.getEmail(), headerAccessor.getMessageHeaders());
    }

}