package cz.cardgames.fortressduel.adapters.ws.protocol;

public record LoggedIn(String token, String name, String playerId) implements Message {
}
