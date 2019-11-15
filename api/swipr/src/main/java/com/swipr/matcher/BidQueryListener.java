package com.swipr.matcher;

import com.swipr.utils.UserSessionManager;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

public class BidQueryListener implements SellQueryListener {

    private SimpMessageSendingOperations messagingTemplate;
    private SimpMessageHeaderAccessor header;

    // Look up ID and send to specific user
    private UserSessionManager sessionManager = UserSessionManager.getInstance();

    public BidQueryListener(SimpMessageSendingOperations messagingTemplate, SimpMessageHeaderAccessor header) {
        this.messagingTemplate = messagingTemplate;
        this.header = header;
    }

    @Override
    public void onMatchFound(SellQuery sellQuery) {
        // When a match is found, we need to make sure the information is sent to the correct UserID
        // For a buy query, we want to send all of the matched SellQueries back to the buyer
        // We need to send back a list of SellQueries. Currently, we just send each sell query back one by one -> why not just gather a list?
        messagingTemplate.convertAndSendToUser(header.getSessionId(), "queue/reply", sellQuery, header.getMessageHeaders());
    }

    @Override
    public void onMatchCancelled(SellQuery expiredSellQuery) {

    }
}