package cz.cardgames.fortressduel.adapters.ws.dto;

import com.fasterxml.jackson.databind.JsonNode;

public class Message {
    /** e.g., "Register", "JoinRoom", "PlayCard" ... */
    public String type;
    /** Room to which the message belongs (optional for Register). */
    public String roomId;
    /** Client-provided correlation id to pair request/response. */
    public String correlationId;
    /** Raw JSON payload; we deserialize it per-type to a specific class. */
    public JsonNode payload;

    // no-arg constructor for Jackson
    public Message() {}
}