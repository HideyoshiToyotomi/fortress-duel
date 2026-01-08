package cz.cardgames.fortressduel.domain.game;

import cz.cardgames.fortressduel.domain.model.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CardPool {

    public static final int MAX_SIZE = 8;

    private final List<Card> cards = new ArrayList<>();

    public CardPool(List<Card> initialCards) {
        if (initialCards.size() > MAX_SIZE) {
            throw new IllegalArgumentException("CardPool can have at most " + MAX_SIZE + " cards");
        }
        cards.addAll(initialCards);
    }

    public List<Card> cards() {
        return Collections.unmodifiableList(cards);
    }

    public int size() {
        return cards.size();
    }
}
