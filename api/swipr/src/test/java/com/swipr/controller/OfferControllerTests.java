package com.swipr.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.swipr.matcher.SellQuery;
import com.swipr.models.Seller;
import com.swipr.utils.UserSessionManager;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@SpringBootTest
public class OfferControllerTests {

    private static final String WS_URL = "ws://localhost:3000/index";
    private static final String UPDATE_OFFER_TOPIC = "/user/queue/sellerUpdate";
    private static final String UPDATE_OFFER_ENDPOINT = "/swipr/updateOffer";
   //  private UserSessionManager userSessionManager = UserSessionManager.getInstance();

    private CompletableFuture<String> messageCompletableFuture;

    @Before
    public void setup() {
        messageCompletableFuture = new CompletableFuture<>();
    }

    @Test
    public void testUpdateOffers() throws URISyntaxException, InterruptedException, ExecutionException, TimeoutException {
        /* Mock sessions */
        /* WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        SellQuery sq = new SellQuery(7, 100, 150, 700, 1);

        stompClient.connect(WS_URL, new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                session.subscribe(UPDATE_OFFER_TOPIC, this);
                SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
                headerAccessor.setSessionId(session.getSessionId());
                headerAccessor.setLeaveMutable(true);
                userSessionManager.addSellerSession(new Seller(7, "Luca", "Matsumoto", "luca@matsumoto.us"), headerAccessor);               
                session.send(UPDATE_OFFER_ENDPOINT, sq);
            }
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                messageCompletableFuture.complete((String) payload);
            }
        }).get(1, TimeUnit.SECONDS);

        String message = messageCompletableFuture.get(15, TimeUnit.SECONDS);

        assertNotNull(message);
        assertEquals("Offer successfully updated", message); */
    }

}

