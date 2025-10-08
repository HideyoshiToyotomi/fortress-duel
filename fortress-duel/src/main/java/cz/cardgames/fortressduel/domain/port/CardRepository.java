package cz.cardgames.fortressduel.domain.port;

import cz.cardgames.fortressduel.domain.model.Card;

import java.util.List;
import java.util.Optional;

/**
 * Domain port for accessing card definitions.
 * Implementations (JSON, DB, in-memory...) belong to adapters.
 */
public interface CardRepository {
    List<Card> loadAll();
    Optional<Card> findById(String id);
}