package cz.cardgames.fortressduel.adapters.ws.dto;

public class Registered {
    public String type = "Registered";
    public String playerId;
    public String correlationId;
    public Registered(String playerId, String correlationId) {
        this.playerId = playerId;
        this.correlationId = correlationId;
    }
}
