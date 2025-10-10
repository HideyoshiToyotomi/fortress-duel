package cz.cardgames.fortressduel.adapters.ws.protocol;

public record Login(String name, String password) implements Message {
}
