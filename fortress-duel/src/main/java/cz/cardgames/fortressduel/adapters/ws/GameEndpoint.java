package cz.cardgames.fortressduel.adapters.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/ws/game")
public class GameEndpoint {

    private static final Map<String, Session> playersById = new ConcurrentHashMap<>();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Game WS connected: " + session.getId());
        // Optional: set idle timeout etc.
        session.setMaxIdleTimeout(60_000L * 10); // 10 minutes
    }

    @OnMessage
    public void onMessage(String text, Session session) throws IOException {
        JsonNode root;
        try {
            root = MAPPER.readTree(text);
        } catch (IOException e) {
            send(session, "{\"type\":\"error\",\"message\":\"invalid json\"}");
            return;
        }

        String type = root.path("type").asText(null);
        if (type == null) {
            send(session, "{\"type\":\"error\",\"message\":\"missing type\"}");
            return;
        }

        switch (type) {
            case "register" -> handleRegister(root, session);
            default -> send(session, "{\"type\":\"error\",\"message\":\"unknown type\"}");
        }
    }

    private void handleRegister(JsonNode msg, Session session) throws IOException {
        String playerId = msg.path("playerId").asText(null);
        String name = msg.path("name").asText(null);
        if (playerId == null || playerId.isBlank()) {
            send(session, "{\"type\":\"error\",\"message\":\"playerId required\"}");
            return;
        }

        // Store mapping playerId -> session
        playersById.put(playerId, session);

        // Keep some info on Session, useful later (tables etc.)
        session.getUserProperties().put("playerId", playerId);
        session.getUserProperties().put("name", name);

        send(session, "{\"type\":\"registered\",\"playerId\":\"" + playerId + "\"}");
        System.out.println("Registered player " + playerId + " (" + name + ") on session " + session.getId());
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        // Remove from registry if present
        Object pid = session.getUserProperties().get("playerId");
        if (pid instanceof String p) {
            playersById.remove(p, session);
        }
        System.out.println("Game WS closed: " + session.getId() + " reason=" + reason);
    }

    @OnError
    public void onError(Session session, Throwable t) {
        System.err.println("Game WS error: " + (session != null ? session.getId() : "no-session") + " -> " + t);
    }

    private static void send(Session s, String json) throws IOException {
        s.getBasicRemote().sendText(json);
    }
}
