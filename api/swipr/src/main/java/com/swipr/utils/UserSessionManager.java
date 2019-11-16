package com.swipr.utils;

import java.util.HashMap;
import java.util.Map;

import com.swipr.models.User;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

/**
 * Singleton helper object to manage each User's sessions 
 */
public class UserSessionManager {

    private static volatile UserSessionManager helperInstance;

    private Map<User, SimpMessageHeaderAccessor> userSessions;
    
    private UserSessionManager() {
        userSessions = new HashMap<>();
    }

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

    /**
     * Add a session ID associated to a particular user
     * @param user the user that sent the request
     * @param headerAccessor header attached to the request to retrieve session ID from 
     */
    public void addSession(User user, SimpMessageHeaderAccessor headerAccessor) {
        userSessions.put(user, headerAccessor);
    }

    public User getUserFromSessionId(SimpMessageHeaderAccessor headerAccessor) {
        for (Map.Entry<User, SimpMessageHeaderAccessor> entry: userSessions.entrySet()) {
            if (entry.getValue().getSessionId().equals(headerAccessor.getSessionId())) {
                return entry.getKey();
            }
        }
        return null;
    }
}