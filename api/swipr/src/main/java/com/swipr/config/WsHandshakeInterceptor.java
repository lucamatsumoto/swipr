package com.swipr.config;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * Class for intercepting websocket connection requests to manager the session ID of the client
 */
public class WsHandshakeInterceptor implements HandshakeInterceptor {

	/**
     * Adds a unique session ID attribute to each user
     * @param request Request coming from the client
	 * @param response Response outgoing to the client
	 * @param wsHandler the registered Websocket handler 
	 * @param attributes A map of attributes that pertain to a particular user's request
     */
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map attributes) throws Exception {
		if (request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
			HttpSession session = servletRequest.getServletRequest().getSession();
			// We need to somehow get the userID in the header
			attributes.put("sessionId", session.getId());
		}
		return true;
	}

	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception ex) {
	}
}