package cz.cardgames.fortressduel.adapters.ws.protocol;

public record Register(String name, String password) implements Message {
}
