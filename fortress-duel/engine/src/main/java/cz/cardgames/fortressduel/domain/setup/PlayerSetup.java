package cz.cardgames.fortressduel.domain.setup;

import java.util.Objects;

public record PlayerSetup(String playerId, String teamId, DeckRef deckRef) {
    public PlayerSetup {
        Objects.requireNonNull(playerId);
        Objects.requireNonNull(deckRef);
    }
}
