package cz.cardgames.fortressduel.adapters.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import cz.cardgames.fortressduel.adapters.persistence.db.H2PlayerRepository;
import cz.cardgames.fortressduel.application.security.Passwords;
import cz.cardgames.fortressduel.domain.port.PlayerRepository;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/ws/game")
public class GameEndpoint {

    private static final Map<String, String> tokens = new ConcurrentHashMap<>();
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final PlayerRepository PLAYERS = new H2PlayerRepository();

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Game WS connected: " + session.getId());
        session.setMaxIdleTimeout(60_000L * 10); // 10 minutes
    }

    @OnMessage
    public void onMessage(String text, Session session) throws IOException {
        JsonNode root;
        try {
            root = MAPPER.readTree(text);
            String type = getRequiredTextFromNode(root, "type");
            switch (type.toLowerCase(Locale.ROOT)) {
                case "register" -> handleRegister(root, session);
                case "login" -> handleLogin(root, session);
                default -> sendError(session, "unknown type: " + type);
            }
        } catch (IOException e) {
            sendError(session, "invalid json");
        } catch (IllegalArgumentException e) {
            sendError(session, e.getMessage());
        }
    }

    private void handleLogin (JsonNode msg, Session session) throws IOException {
        String name = getRequiredTextFromNode(msg, "name");
        String password = getRequiredTextFromNode(msg, "password");

        String storedHash = PLAYERS.findPassHashByName(name);
        if (storedHash == null) {
            sendError(session, "invalid credentials");
            return;
        }

        boolean ok = Passwords.verify(password.toCharArray(), storedHash);
        if (!ok) {
            sendError(session, "invalid credentials");
            return;
        }

        String token = UUID.randomUUID().toString();
        tokens.put(token, name); // for MVP we map token -> name

        // Respond with a login acknowledgment
        sendJson(session, """
        {"type":"loggedIn","token":"%s","name":"%s"}
        """.formatted(token, name));

    }

    private void handleRegister(JsonNode msg, Session session) throws IOException {
        String name = getRequiredTextFromNode(msg, "name");
        String password = getRequiredTextFromNode(msg, "password");

        if (password.length() < 6) {
            sendError(session, "password too short (min 6 characters)");
            return;
        }

        if (PLAYERS.existsByName(name)) {
            sendError(session, "name already exists");
            return;
        }

        char[] pwd = password.toCharArray();
        String pwdHash = Passwords.hash(pwd);
        Arrays.fill(pwd, '\0');

        String playerId = UUID.randomUUID().toString();
        try {
            PLAYERS.create(playerId, name, pwdHash);
        } catch (Exception e) {
            sendError(session, "failed to create player");
            return;
        }

        sendJson(session, """
            {"type":"registered","playerId":"%s"}
            """.formatted(playerId));

        System.out.println("Registered player (" + name + ") -> " + playerId + " on session " + session.getId());
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println("Game WS closed: " + session.getId() + " reason=" + reason);
    }

    @OnError
    public void onError(Session session, Throwable t) {
        System.err.println("Game WS error: " + (session != null ? session.getId() : "no-session") + " -> " + t);
    }

    private static void sendJson(Session session, String json) throws IOException {
        session.getBasicRemote().sendText(json);
    }

    private static void sendError(Session session, String error) throws IOException {
        String json = MAPPER.createObjectNode()
                .put("type", "error")
                .put("message", error)
                .toString();
        sendJson(session, json);
    }

    private static String getOptionalTextFromNode(JsonNode node, String fieldName) {
        JsonNode v = node.get(fieldName);
        return (v == null || v.isNull()) ? null : v.asText();
    }

    private static String getRequiredTextFromNode(JsonNode node, String fieldName) {
        String value = getOptionalTextFromNode(node, fieldName);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Missing or blank field: " + fieldName);
        }
        return value;
    }
}
