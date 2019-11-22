package com.swipr.utils;

import java.util.HashMap;
import java.util.Map;

import com.swipr.models.Buyer;
import com.swipr.models.Seller;
import com.swipr.models.User;

import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageType;

/**
 * Singleton object to manage the core networking logic of our application 
 */
public class UserSessionManager {

    private static volatile UserSessionManager helperInstance;

    private Map<Buyer, SimpMessageHeaderAccessor> buyerSessions;
    private Map<Seller, SimpMessageHeaderAccessor> sellerSessions;

    
    private UserSessionManager() {
        this.buyerSessions = new HashMap<>();
        this.sellerSessions = new HashMap<>();
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
    public void addBuyerSession(Buyer buyer, SimpMessageHeaderAccessor headerAccessor) {
        buyerSessions.put(buyer, headerAccessor);
    }

    public void addSellerSession(Seller seller, SimpMessageHeaderAccessor headerAccessor) {
        sellerSessions.put(seller, headerAccessor);
    }

    public Buyer getBuyerFromSessionId(User user, SimpMessageHeaderAccessor headerAccessor) {
        for (Map.Entry<Buyer, SimpMessageHeaderAccessor> entry: buyerSessions.entrySet()) {
            if (entry.getValue().getSessionId().equals(headerAccessor.getSessionId())) {
                return entry.getKey();
            }
        }
        // Hasn't been found yet
        Buyer buyer = new Buyer(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
        buyer.setVenmo(user.getVenmo());
        buyer.setProfilePicUrl(user.getProfilePicUrl());
        return buyer;
    }

    public Seller getSellerFromSessionId(User user, SimpMessageHeaderAccessor headerAccessor) {
        for (Map.Entry<Seller, SimpMessageHeaderAccessor> entry: sellerSessions.entrySet()) {
            if (entry.getValue().getSessionId().equals(headerAccessor.getSessionId())) {
                return entry.getKey();
            }
        }
        Seller seller = new Seller(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
        seller.setVenmo(user.getVenmo());
        seller.setProfilePicUrl(user.getProfilePicUrl());
        return seller;
    }

    public SimpMessageHeaderAccessor getSellerHeaders(User seller) {
        for (Map.Entry<Seller, SimpMessageHeaderAccessor> entry: sellerSessions.entrySet()) {
            if (entry.getKey().getEmail().equals(seller.getEmail())) {
                return entry.getValue();
            }
        }
        return null;
    }

    public SimpMessageHeaderAccessor getBuyerHeaders(User buyer) {
        for (Map.Entry<Buyer, SimpMessageHeaderAccessor> entry: buyerSessions.entrySet()) {
            if (entry.getKey().getEmail().equals(buyer.getEmail())) {
                return entry.getValue();
            }
        }
        return null;   
    }

    public void sendToUser(SimpMessageHeaderAccessor headerAccessor, String topic, Object message, SimpMessageSendingOperations messagingTemplate) {
        messagingTemplate.convertAndSendToUser(headerAccessor.getSessionId(), topic, message, createHeaders(headerAccessor.getSessionId()));
    }

    public void sendToMultipleUsers(String topic, Object message, SimpMessageSendingOperations messagingTemplate) {
        messagingTemplate.convertAndSend(topic, message);
    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }
}