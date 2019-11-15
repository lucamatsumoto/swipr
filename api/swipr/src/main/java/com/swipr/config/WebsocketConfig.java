package com.swipr.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.standard.TomcatRequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

/**
 * Class for configuring websockets and their respective endpoints and topics
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer{

    /**
     * Configures the topics for the client to subscribe to
     * @param registry A default message broker registry that handles registering a pub sub message broker for our server
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic/", "/queue/", "/user");
        registry.setApplicationDestinationPrefixes("/swipr");
        registry.setUserDestinationPrefix("/user");
    }

    /**
     * Configures the base endpoints and request interceptors for the server to use
     * @param registry object to register the endpoints with STOMP
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/index").addInterceptors(new WsHandshakeInterceptor()).setHandshakeHandler(new DefaultHandshakeHandler(new TomcatRequestUpgradeStrategy())).setAllowedOrigins("*");
    }
}