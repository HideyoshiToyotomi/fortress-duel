package cz.cardgames.fortressduel.domain.setup;

public record DeckRef(String id) {
    public static DeckRef basic(String id) { return new DeckRef("basic:" + id); }
    public static DeckRef custom(String id) { return new DeckRef("custom:" + id); }
}
