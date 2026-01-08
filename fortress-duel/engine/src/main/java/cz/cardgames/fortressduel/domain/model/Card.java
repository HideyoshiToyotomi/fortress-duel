package cz.cardgames.fortressduel.domain.model;

/**
 * Domain model for a card definition.
 * - id: stable human-readable slug used across JSON, assets and tests (e.g., "stone_wall")
 * - name: display name (localizable later)
 * - type: coarse category for UI/filters; behavior is driven by effects (to be added)
 */
public class Card {
    private String id;
    private String name;
    private CardType type;

    // Required by JSON mappers (Jackson) and some frameworks
    public Card() { }

    public Card(String id, String name, CardType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public CardType getType() { return type; }

    // Keep setters for now to make JSON deserialization straightforward
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setType(CardType type) { this.type = type; }

    @Override
    public String toString() {
        return "Card{id='%s', name='%s', type=%s}".formatted(id, name, type);
    }
}