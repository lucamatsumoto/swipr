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

    private final SimpMessageSendingOperations messagingTemplate;


    private Map<Buyer, SimpMessageHeaderAccessor> buyerSessions;
    private Map<Seller, SimpMessageHeaderAccessor> sellerSessions;


    private UserSessionManager(SimpMessageSendingOperations messageTemplate) {
        this.messagingTemplate = messageTemplate;
        this.buyerSessions = new HashMap<>();
        this.sellerSessions = new HashMap<>();
    }

    public static UserSessionManager getInstance(SimpMessageSendingOperations messageTemplate) {
        if (helperInstance == null) {
            synchronized(UserSessionManager.class) {
                if (helperInstance == null) {
                    helperInstance = new UserSessionManager(messageTemplate);
                }
            }
        }
        return helperInstance;
    }

    /**
     * Add a session ID associated to a particular buyer
     * @param buyer the buyer that sent the request
     * @param headerAccessor header attached to the request to retrieve session ID from 
     */
    public void addBuyerSession(Buyer buyer, SimpMessageHeaderAccessor headerAccessor) {
        buyerExists(buyer);
        buyerSessions.put(buyer, headerAccessor);
    }

    /**
     * Add a session ID associated to a particular seller
     * @param seller the seller buyer that sent the request
     * @param headerAccessor header attached to the request to retrieve session ID from 
     */
    public void addSellerSession(Seller seller, SimpMessageHeaderAccessor headerAccessor) {
        sellerExists(seller);
        sellerSessions.put(seller, headerAccessor);
    }

    /**
     * Get the buyer from a particular session id and user object from the database
     * @param user the user object found from the database
     * @param headerAccessor header attached to the request to retrieve session ID from 
     * @return the buyer that was found
     */
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

    /**
     * Get the seller from a particular session id and user object from the database
     * @param user the user object found from the databases
     * @param headerAccessor header attached to the request to retrieve session ID from 
     * @return the seller that was found
     */
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

    /**
     * Get the header information of a particular seller
     * @param seller the seller information from the database
     * @return the headers attached to a particular user
     */
    public SimpMessageHeaderAccessor getSellerHeaders(User seller) {
        for (Map.Entry<Seller, SimpMessageHeaderAccessor> entry: sellerSessions.entrySet()) {
            if (entry.getKey().getEmail().equals(seller.getEmail())) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * Get the header information of a particular buyer
     * @param buyer the buyer information from the database
     * @return the headers attached to a particular user
     */
    public SimpMessageHeaderAccessor getBuyerHeaders(User buyer) {
        for (Map.Entry<Buyer, SimpMessageHeaderAccessor> entry: buyerSessions.entrySet()) {
            if (entry.getKey().getEmail().equals(buyer.getEmail())) {
                return entry.getValue();
            }
        }
        return null;   
    }

    /**
     * Wrapper method to interface sending a particular object to a particular user
     * @param headerAccessor the headers of the user to send the information to 
     * @param topic the topic that the message should be sent to
     * @param message the message that should be sent to the user
     */
    public void sendToUser(SimpMessageHeaderAccessor headerAccessor, String topic, Object message) {
        messagingTemplate.convertAndSendToUser(headerAccessor.getSessionId(), topic, message, createHeaders(headerAccessor.getSessionId()));
    }

    /**
     * Wrapper method to interface sending a particular object to all users that are subscribed to a topic
     * @param topic the topic that the message should be sent to 
     * @param message the message that should be sent to the user
     */
    public void sendToMultipleUsers(String topic, Object message) {
        messagingTemplate.convertAndSend(topic, message);
    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }

    // Since you cannot not when a user disconnected, we will check whether the ID already exists to refresh
    private void sellerExists(User user) {
        for (Map.Entry<Seller, SimpMessageHeaderAccessor> entry: sellerSessions.entrySet()) {
            if (user.getId().equals(entry.getKey().getId())) {
                sellerSessions.remove(entry.getKey());
                break;
            }
        }
    }

    private void buyerExists(User user) {
        for (Map.Entry<Buyer, SimpMessageHeaderAccessor> entry: buyerSessions.entrySet()) {
            if (user.getId().equals(entry.getKey().getId())) {
                buyerSessions.remove(entry.getKey());
                break;
            }
        }
    }
}