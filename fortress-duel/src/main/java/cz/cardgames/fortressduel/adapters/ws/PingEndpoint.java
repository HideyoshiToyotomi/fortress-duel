package cz.cardgames.fortressduel.adapters.ws;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

/**
 * Minimal WebSocket endpoint for quick sanity check.
 * URL path: /ws/ping
 *
 * Client sends text "ping"  -> server replies "pong".
 * Any other message is echoed back with "echo: <message>".
 */
@ServerEndpoint("/ws/ping")
public class PingEndpoint {

    @OnOpen
    public void onOpen(Session session) {
        // Called when a new WebSocket connection is established
        System.out.println("WS connected: " + session.getId());
    }

    @OnMessage
    public String onMessage(String message, Session session) {
        // Called when a text message arrives from the client
        if ("ping".equalsIgnoreCase(message.trim())) {
            return "pong";
        }
        return "echo: " + message;
    }

    @OnClose
    public void onClose(Session session) {
        // Called when the client closes the connection
        System.out.println("WS closed: " + session.getId());
    }
}

