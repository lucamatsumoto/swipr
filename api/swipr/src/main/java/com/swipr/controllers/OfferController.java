package com.swipr.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.swipr.models.Offer;
import com.swipr.repository.UserRepository;
import com.swipr.utils.UserSessionManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class OfferController {
    
    // For getting user information from the database, ex: userRepository.findByEmail()
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    private UserSessionManager userSessionManager = UserSessionManager.getInstance();

    // Map of userID to Offer
    private Map<Integer, Offer> activeOffers = new HashMap<>();

    @MessageMapping("/updateOffer") 
    @SendToUser("/queue/reply")
    public void updateOffer(SimpMessageHeaderAccessor headerAccessor, Offer offer) {
        // Update the offers 
        activeOffers.put(offer.getUser().getId(), offer);
        userSessionManager.addSession(offer.getUser(), headerAccessor);
        messagingTemplate.convertAndSendToUser(headerAccessor.getSessionId(), "/queue/reply", "Offer successfully updated", headerAccessor.getMessageHeaders());
    }

    @MessageMapping("/findOffers")
    @SendToUser
    public void findOffers(SimpMessageHeaderAccessor headerAccessor, Offer offer) {

    }

    @MessageMapping("/showInterest")
    @SendToUser
    public void showInterest(SimpMessageHeaderAccessor headerAccessor) {

    }

    @MessageMapping("/cancelOffer")
    @SendToUser
    public void cancelOffer(SimpMessageHeaderAccessor headerAccessor) {

    }

}