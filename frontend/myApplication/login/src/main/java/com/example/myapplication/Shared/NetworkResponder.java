package com.example.myapplication.Shared;

/**
 * Used for both registering callbacks with the NetworkManager and with the SocketService.
 */
public interface NetworkResponder {
    /**
     * Upon receiving a response from the server, the NetworkManager checks for any
     * subscribers, and invokes onMessageReceived on any that exists.
     * @param json  The payload of the response from the server.
     */
    void onMessageReceived(String json);
}
