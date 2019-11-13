package com.swipr.controllers;

import java.util.HashMap;
import java.util.Map;

import com.swipr.models.Offer;
import com.swipr.repository.UserRepository;
import com.swipr.utils.UserSessionManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;


/**
 *  Represents a Websocket handler for creating, deleting, and retrieving user information
 */
@Controller
public class OfferController {
    
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
     * Map of UserID's to associated Offers that are stored in RAM for now.
     */
    private Map<Integer, Offer> activeOffers = new HashMap<>();

    /**
     * Update the offer corresponding to a particular seller
     * @param headerAccessor header object that is sent with every request
     * @param offer Offer that the seller wants to update
     */
    @MessageMapping("/updateOffer") 
    @SendToUser("/queue/reply")
    public void updateOffer(SimpMessageHeaderAccessor headerAccessor, Offer offer) {
        // Update the offers 
        activeOffers.put(offer.getUser().getId(), offer);
        userSessionManager.addSession(offer.getUser(), headerAccessor);
        messagingTemplate.convertAndSendToUser(headerAccessor.getSessionId(), "/queue/reply", "Offer successfully updated", headerAccessor.getMessageHeaders());
    }

    /**
     * Find a particular offer based on parameters
     * @param headerAccessor header object that is sent with every request
     * @param offer Offer that the seller wants to update
     */
    @MessageMapping("/findOffers")
    @SendToUser
    public void findOffers(SimpMessageHeaderAccessor headerAccessor, Offer offer) {

    }

    /**
     * Endpoint for a buyer to indicate interest in a seller's offer
     * @param headerAccessor header object that is sent with every request
     */
    @MessageMapping("/showInterest")
    @SendToUser
    public void showInterest(SimpMessageHeaderAccessor headerAccessor) {

    }

    /**
     * Cancel an offer if expired or if user cancels
     * @param headerAccessor header object that is sent with every request
     */
    @MessageMapping("/cancelOffer")
    @SendToUser
    public void cancelOffer(SimpMessageHeaderAccessor headerAccessor) {

    }

}