package cz.cardgames.fortressduel.adapters.persistence.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cardgames.fortressduel.domain.model.Card;
import cz.cardgames.fortressduel.domain.port.CardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.*;

public class JsonCardRepository implements CardRepository {
    private static final Logger log = LoggerFactory.getLogger(JsonCardRepository.class);

    private final String resourcePath; // e.g., "/data/cards.json" on classpath
    private final ObjectMapper mapper = new ObjectMapper();
    private List<Card> cache = null; // simple lazy cache

    public JsonCardRepository(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    @Override
    public synchronized List<Card> loadAll() {
        if (cache != null) {
            return cache;
        }
        log.info("Loading cards from resource: {}", resourcePath);
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IllegalStateException("Resource not found on classpath: " + resourcePath);
            }
            List<Card> cards = mapper.readValue(is, new TypeReference<List<Card>>() {});
            validate(cards);
            cache = Collections.unmodifiableList(cards);
            log.info("Loaded {} cards.", cache.size());
            return cache;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load card definitions: " + resourcePath, e);
        }
    }

    @Override
    public Optional<Card> findById(String id) {
        return loadAll().stream().filter(c -> Objects.equals(c.getId(), id)).findFirst();
    }

    private void validate(List<Card> cards) {
        // Basic validation: unique ids
        Set<String> seen = new HashSet<>();
        for (Card c : cards) {
            if (c.getId() == null || c.getId().isBlank()) {
                throw new IllegalStateException("Card id must not be null/blank.");
            }
            if (!seen.add(c.getId())) {
                throw new IllegalStateException("Duplicate card id: " + c.getId());
            }
            if (c.getName() == null || c.getName().isBlank()) {
                throw new IllegalStateException("Card name must not be null/blank. id=" + c.getId());
            }
            if (c.getType() == null) {
                throw new IllegalStateException("Card type must not be null. id=" + c.getId());
            }
        }
    }
}