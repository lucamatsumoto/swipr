package com.swipr.controllers;

import java.util.Set;

import com.swipr.matcher.BuyQuery;
import com.swipr.matcher.Matchmaker;
import com.swipr.matcher.SellQuery;
import com.swipr.models.Buyer;
import com.swipr.models.Interest;
import com.swipr.models.Seller;
import com.swipr.models.User;
import com.swipr.repository.UserRepository;
import com.swipr.utils.AverageSwipePrice;
import com.swipr.utils.UserSessionManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
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


    /**
     * Gets the average daily price of the offers. Everytime a new offer is added, we update and broadcast to all clients
     * All users, both buyers and sellers, must subscribe to this topic when a connection is opened
     * @param headerAccessor header object that is sent with every request
     */
    @MessageMapping("/averageOffer")
    public void getAverageSellPrice() {
        long averagePrice = AverageSwipePrice.getCents();
        userSessionManager.sendToMultipleUsers("/topic/average", averagePrice, messagingTemplate);
        //messagingTemplate.convertAndSend("/topic/average", averagePrice);
    }

    /**
     * Update the offer corresponding to a particular seller
     * @param headerAccessor header object that is sent with every request
     * @param query Offer query that the seller wants to update
     */
    @MessageMapping("/updateOffer") 
    public void updateOffer(@Payload SellQuery query, SimpMessageHeaderAccessor headerAccessor) {
        // Update the offer / post a new offer
        User user = userRepository.findById(query.userId);
        Seller seller = userSessionManager.getSellerFromSessionId(user, headerAccessor); 
        userSessionManager.addSellerSession(seller, headerAccessor);
        matchMaker.updateSellQuery(query);
        userSessionManager.sendToUser(headerAccessor, "/queue/sellerUpdate", "Offer successfully updated", messagingTemplate);
        // messagingTemplate.convertAndSendToUser(headerAccessor.getSessionId(), "/queue/reply", "Offer successfully updated", headerAccessor.getMessageHeaders());
    }

    /**
     * Find a particular offer based on buyer's query parameters
     * @param headerAccessor header object that is sent with every request
     * @param query Offer query that the seller wants to update
     */
    @MessageMapping("/findOffers")
    public void findOffers(@Payload BuyQuery query, SimpMessageHeaderAccessor headerAccessor) {
        // Find the buyer that matches the query id
        User user = userRepository.findById(query.userId);
        // Need to rethink this because of cast exception from (Buyer) userRepository.findById(). A hack that'll work for now
        Buyer buyer = userSessionManager.getBuyerFromSessionId(user, headerAccessor);
        userSessionManager.addBuyerSession(buyer, headerAccessor);    
        // Add listener to the corresponding user ID
        matchMaker.updateBuyQuery(query, buyer);
        // Retrieve a list of all bids found and send to the user
        userSessionManager.sendToUser(headerAccessor, "/queue/buyerFind", buyer.getMatchedSellQueries(), messagingTemplate);
        // messagingTemplate.convertAndSendToUser(headerAccessor.getSessionId(), "/queue/reply", buyer.getMatchedSellQueries(), headerAccessor.getMessageHeaders());
    }

    /**
     * Refresh the offer without sending extra parameters
     * @param headerAccessor header object that is sent with every request
     */
    @MessageMapping("/refreshOffers")
    public void refreshOffers(SimpMessageHeaderAccessor headerAccessor) {
        // Retrieve user based on the session ID
        Buyer buyer = userSessionManager.getBuyerFromSessionId(null, headerAccessor);
        userSessionManager.sendToUser(headerAccessor, "/queue/buyerFind", buyer.getMatchedSellQueries(), messagingTemplate);
        // messagingTemplate.convertAndSendToUser(headerAccessor.getSessionId(), "/queue/reply", buyer.getMatchedSellQueries(), headerAccessor.getMessageHeaders());
    }

    // Need logic for subscribing to multiple topics. Buyer should be notified in real time when a new sellquery is posted that matches their 
    // buy parameters

    /**
     * Endpoint for a buyer to indicate interest in a seller's offer
     * @param headerAccessor header object that is sent with every request
     */
    @MessageMapping("/showInterest")
    public void showInterest(@Payload Interest interest, SimpMessageHeaderAccessor headerAccessor) {
        // Retrieve the buyer from the sessionId
        User interestedUser = userRepository.findById(interest.getBuyerId());
        Buyer buyer = userSessionManager.getBuyerFromSessionId(interestedUser, headerAccessor);
        // Assuming that the seller is still active
        SellQuery sellQuery = interest.getSellquery();
        User sellingUser = userRepository.findById(sellQuery.userId);
        SimpMessageHeaderAccessor sellingUserHeaders = userSessionManager.getSellerHeaders(sellingUser);
        Seller seller = userSessionManager.getSellerFromSessionId(sellingUser, sellingUserHeaders);
        // userSessionManager.addSellerSession(seller, headerAccessor);
        buyer.indicateInterestInOffer(sellQuery, seller);
        // Re-update buyer and seller sessions
        userSessionManager.addBuyerSession(buyer, headerAccessor);
        userSessionManager.addSellerSession(seller, sellingUserHeaders);
        Set<Buyer> potentialBuyers = seller.getPotentialBuyers();
        // Batch a list of potential buyers and send them to seller
        userSessionManager.sendToUser(sellingUserHeaders, "/queue/sellerInterest", potentialBuyers, messagingTemplate);
    }

    /**
     * 
     * @param buyerId the ID of the user (buyer) who wants to cancel interest in the purchase
     * @param query the offer that they want to cancel
     * @param headerAccessor headers as a part of the request
     */
    @MessageMapping("/cancelInterest")
    public void cancelInterest(@Payload Interest interest, SimpMessageHeaderAccessor headerAccessor) {
        // We need to remove the SellQuery corresponding to the user from the AveragePrice
        User interestedUser = userRepository.findById(interest.getBuyerId());
        Buyer buyer = userSessionManager.getBuyerFromSessionId(interestedUser, headerAccessor);
        // Assuming that the seller is still active
        SellQuery query = interest.getSellquery();
        User sellingUser = userRepository.findById(query.userId);
        SimpMessageHeaderAccessor sellingUserHeaders = userSessionManager.getSellerHeaders(sellingUser);
        Seller seller = userSessionManager.getSellerFromSessionId(sellingUser, sellingUserHeaders);
        // userSessionManager.addSellerSession(seller, headerAccessor);
        buyer.cancelInterestInOffer(query, seller);
        // Re-update buyer and seller sessions
        userSessionManager.addBuyerSession(buyer, headerAccessor);
        userSessionManager.addSellerSession(seller, sellingUserHeaders);
        // Batch a list of potential buyers and send them to seller
        userSessionManager.sendToUser(sellingUserHeaders, "/queue/sellerInterest", seller.getPotentialBuyers(), messagingTemplate);
    }

    /**
     * Seller confirms interest and agrees to sell the swipe
     * @param buyer the buyer that they want to buy from
     * @param headerAccessor the headers that are sent along with the request
     */
    @MessageMapping("/confirmInterest")
    public void confirmInterest(@Payload Buyer buyer, SimpMessageHeaderAccessor headerAccessor) {
        SimpMessageHeaderAccessor buyingUserHeaders = userSessionManager.getBuyerHeaders(buyer);
        // Also remove the buyer from the seller's list and notify the seller
        Seller seller = userSessionManager.getSellerFromSessionId(null, headerAccessor);
        seller.clearPotentialBuyers();
        // Make sure to remove that offer from the list of active offers
        matchMaker.deleteByUserId(seller.getId());
        // We send back the buyer's information, including the venmo 
        User boughtFrom = userRepository.findById(buyer.getId());
        userSessionManager.sendToUser(headerAccessor, "/queue/sellerInterest", boughtFrom, messagingTemplate);
        // When the buyer confirms interest, send out the new average price to all users
        userSessionManager.sendToUser(buyingUserHeaders, "/queue/buyerInterest", seller, messagingTemplate);
        getAverageSellPrice();
    }

    /**
     * Cancel an offer if expired or if user cancels
     * @param headerAccessor header object that is sent with every request
     */
    @MessageMapping("/cancelOffer")
    public void cancelOffer(SimpMessageHeaderAccessor headerAccessor) {
        Seller seller = userSessionManager.getSellerFromSessionId(null, headerAccessor);
        matchMaker.deleteByUserId(seller.getId());
        messagingTemplate.convertAndSendToUser(headerAccessor.getSessionId(), "/queue/sellerCancel", "Your offer has been cancelled", headerAccessor.getMessageHeaders());
    }

    /**
     * Dummy endpoint for testing
     * @param headerAccessor dummy header
     */
    @MessageMapping("/getAllOffers")
    public void getAllOffers(SimpMessageHeaderAccessor headerAccessor) {
        messagingTemplate.convertAndSendToUser(headerAccessor.getSessionId(), "/queue/reply", matchMaker.getActiveSellQueries(), headerAccessor.getMessageHeaders());
    }

    /**
     * 
     * @param user the user you will like to notify
     * @param headerAccessor header object that is sent with every request
     */
    @MessageMapping("/here")
    public void notifyHere(@Payload User user, SimpMessageHeaderAccessor headerAccessor) {

    }

}
