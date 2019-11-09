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

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    // UserSessionManager object contains information about each sessions ID, headers, and other important information and their corresponding User objects
    private UserSessionManager userSessionManager = UserSessionManager.getInstance();

    @MessageMapping("/all")
    @SendToUser("/queue/reply")
    public void getUsers(SimpMessageHeaderAccessor headerAccessor) {
        messagingTemplate.convertAndSendToUser(headerAccessor.getSessionId(), "/queue/reply", userRepository.findAll(), headerAccessor.getMessageHeaders());
    }

    // This will be replaced by authentication later
    @MessageMapping("/create") 
    @SendToUser("/queue/reply")
    public void createUser(@Payload User user, SimpMessageHeaderAccessor headerAccessor) {
        // Make sure to add error handling later as well 
        System.out.println(headerAccessor.getSessionId());
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

    @MessageMapping("/delete")
    @SendToUser("/queue/reply")
    public void deleteUser(@Payload User user, SimpMessageHeaderAccessor headerAccessor) {
        userRepository.delete(user);
        messagingTemplate.convertAndSendToUser(headerAccessor.getSessionId(), "/queue/reply", "deleted user with email " + user.getEmail(), headerAccessor.getMessageHeaders());
    }

}