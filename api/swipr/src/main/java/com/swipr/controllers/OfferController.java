package com.swipr.controllers;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import com.swipr.matcher.BidQueryListener;
import com.swipr.matcher.BuyQuery;
import com.swipr.matcher.Matchmaker;
import com.swipr.matcher.Query;
import com.swipr.matcher.SellQuery;
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
     * Matchmaker object for handling all offer business logic
     */
    private Matchmaker matchMaker = Matchmaker.getInstance();


    /**
     * Gets the average daily price of the offers. Everytime a new offer is added, we update and broadcast to all clients
     * @param headerAccessor header object that is sent with every request
     */
    @MessageMapping("/averageOffer")
    @SendTo("/topic/average")
    public void getAverageSellPrice(SimpMessageHeaderAccessor headerAccessor) {
        ArrayList<SellQuery> activeOffers = matchMaker.getActiveSellQueries();
        ArrayList<Long> dailyPrices = new ArrayList<>();
        // Figure out timestamps later
        LocalDateTime date = LocalDateTime.now();
        for (int i = 0; i < activeOffers.size(); i++) {
            LocalDateTime offerDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(activeOffers.get(i).timeRangeEnd), TimeZone.getTimeZone("America/Los_Angeles").toZoneId());
            // If the day of the year and the year the offer is in are the same, then they exist on the same day
            if (offerDate.getDayOfYear() == date.getDayOfYear() && offerDate.getYear() == date.getYear()) {
                dailyPrices.add(new Long(activeOffers.get(i).priceCents));
            }
        }
        long totalPrice = 0;
        for (Long price: dailyPrices) {
            totalPrice += price;
        }
        messagingTemplate.convertAndSend("/topic/average", totalPrice/dailyPrices.size());
    }


    /**
     * Update the offer corresponding to a particular seller
     * @param headerAccessor header object that is sent with every request
     * @param query Offer query that the seller wants to update
     */
    @MessageMapping("/updateOffer") 
    @SendToUser("/queue/reply")
    public void updateOffer(SimpMessageHeaderAccessor headerAccessor, SellQuery query) {
        // Update the offer / post a new offer
        // userSessionManager.addSession(offer.getUser(), headerAccessor);
        matchMaker.updateSellQuery(query);
        // Don't know if this works 
        getAverageSellPrice(headerAccessor);
        messagingTemplate.convertAndSendToUser(headerAccessor.getSessionId(), "/queue/reply", "Offer successfully updated", headerAccessor.getMessageHeaders());
    }

    /**
     * Find a particular offer based on buyer's query parameters
     * @param headerAccessor header object that is sent with every request
     * @param query Offer query that the seller wants to update
     */
    @MessageMapping("/findOffers")
    @SendToUser
    public void findOffers(SimpMessageHeaderAccessor headerAccessor, BuyQuery query) {
        matchMaker.updateBuyQuery(query, new BidQueryListener(messagingTemplate, headerAccessor));
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