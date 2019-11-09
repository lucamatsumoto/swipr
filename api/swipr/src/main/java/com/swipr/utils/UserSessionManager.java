package com.swipr.utils;

import java.util.HashMap;
import java.util.Map;

import com.swipr.models.User;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

public class UserSessionManager {

    private static volatile UserSessionManager helperInstance;

    private Map<User, SimpMessageHeaderAccessor> userSessions;
    
    private UserSessionManager() {
        userSessions = new HashMap<>();
    }

    /*
        Thread Safe Singleton helper class for helper functions :)
    */
    public static UserSessionManager getInstance() {
        if (helperInstance == null) {
            synchronized(UserSessionManager.class) {
                if (helperInstance == null) {
                    helperInstance = new UserSessionManager();
                }
            }
        }
        return helperInstance;
    }

    // Add a session to the list of sessions and associated users
    public void addSession(User user, SimpMessageHeaderAccessor headerAccessor) {
        userSessions.put(user, headerAccessor);
    }
}