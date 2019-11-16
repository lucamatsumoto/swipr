package com.swipr.controllers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import com.swipr.matcher.BidQueryListener;
import com.swipr.matcher.BuyQuery;
import com.swipr.matcher.Matchmaker;
import com.swipr.matcher.SellQuery;
import com.swipr.models.User;
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
      
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    private UserSessionManager userSessionManager = UserSessionManager.getInstance();

    private Matchmaker matchMaker = Matchmaker.getInstance();

    // Map of UserID to listeners 
    private Map<Integer, BidQueryListener> userListeners = new HashMap<>();


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
        // getAverageSellPrice(headerAccessor);
        messagingTemplate.convertAndSendToUser(headerAccessor.getSessionId(), "/queue/reply", "Offer successfully updated", headerAccessor.getMessageHeaders());
    }

    /**
     * Find a particular offer based on buyer's query parameters
     * @param headerAccessor header object that is sent with every request
     * @param query Offer query that the seller wants to update
     */
    @MessageMapping("/findOffers")
    @SendToUser("/queue/reply")
    public void findOffers(SimpMessageHeaderAccessor headerAccessor, BuyQuery query) {
        BidQueryListener listener = new BidQueryListener();
        // Add listener to the corresponding user ID
        userListeners.put(query.userId, listener);
        matchMaker.updateBuyQuery(query, listener);
        // Retrieve a list of all bids found and send to the user
        messagingTemplate.convertAndSendToUser(headerAccessor.getSessionId(), "/queue/reply", listener.getSellQueryList(), headerAccessor.getMessageHeaders());
    }

    @MessageMapping("/refreshOffers")
    @SendToUser("/queue/reply")
    public void refreshOffers(SimpMessageHeaderAccessor headerAccessor) {
        // Retrieve user based on the session ID
        User user = userSessionManager.getUserFromSessionId(headerAccessor);
        BidQueryListener listener = userListeners.get(user.getId());
        messagingTemplate.convertAndSendToUser(headerAccessor.getSessionId(), "/queue/reply", listener.getSellQueryList(), headerAccessor.getMessageHeaders());
    }

    // Need logic for subscribing to multiple topics. Buyer should be notified in real time when a new sellquery is posted that matches their 
    // buy parameters

    /**
     * Endpoint for a buyer to indicate interest in a seller's offer
     * @param headerAccessor header object that is sent with every request
     */
    @MessageMapping("/showInterest")
    @SendToUser
    public void showInterest(SimpMessageHeaderAccessor headerAccessor) {

    }

    @MessageMapping("/cancelInterest")
    @SendToUser
    public void cancelInterest(SimpMessageHeaderAccessor headerAccessor) {

    }

    /**
     * Cancel an offer if expired or if user cancels
     * @param headerAccessor header object that is sent with every request
     */
    @MessageMapping("/cancelOffer")
    @SendToUser("/queue/reply")
    public void cancelOffer(SimpMessageHeaderAccessor headerAccessor) {
        User user = userSessionManager.getUserFromSessionId(headerAccessor);
        matchMaker.deleteByUserId(user.getId());
        messagingTemplate.convertAndSendToUser(headerAccessor.getSessionId(), "/queue/reply", "Your offer has been cancelled", headerAccessor.getMessageHeaders());
    }

}