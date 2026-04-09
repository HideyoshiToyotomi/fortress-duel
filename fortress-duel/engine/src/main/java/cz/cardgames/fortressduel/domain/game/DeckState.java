package cz.cardgames.fortressduel.domain.game;

import cz.cardgames.fortressduel.domain.model.Card;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

public final class DeckState {
    private final RandomProvider rng;
    private final Deque<Card> drawPile = new ArrayDeque<>();
    private final Deque<Card> discardPile = new ArrayDeque<>();

    public DeckState(List<Card> initialCards, RandomProvider rng) {
        Objects.requireNonNull(initialCards);
        if (initialCards.isEmpty()) {
            throw new IllegalArgumentException("Deck cannot be empty");
        }
        this.rng = Objects.requireNonNull(rng);

        List<Card> copy = new ArrayList<>(initialCards);
        shuffleInPlace(copy, this.rng);

        for (int i = copy.size() - 1; i >= 0; i--) {
            drawPile.push(copy.get(i));
        }
    }

    public Card drawOne() {
        if (drawPile.isEmpty()) {
            reshuffleDiscardIntoDraw();
        }
        if (drawPile.isEmpty()) {
            throw new IllegalStateException("Cannot draw: draw pile and discard pile are empty");
        }
        return drawPile.pop();
    }

    public void discard(Card card) {
        discardPile.push(Objects.requireNonNull(card));
    }

    public int drawSize() { return drawPile.size(); }
    public int discardSize() { return discardPile.size(); }

    private void reshuffleDiscardIntoDraw() {
        if (discardPile.isEmpty()) return;

        List<Card> tmp = new ArrayList<>(discardPile.size());
        while (!discardPile.isEmpty()) {
            tmp.add(discardPile.pop());
        }

        shuffleInPlace(tmp, rng);

        for (int i = tmp.size() - 1; i >= 0; i--) {
            drawPile.push(tmp.get(i));
        }
    }

    static <T> void shuffleInPlace(List<T> list, RandomProvider rng) {
        for (int i = list.size() - 1; i > 0; i--) {
            int j = rng.nextInt(i + 1);
            T tmp = list.get(i);
            list.set(i, list.get(j));
            list.set(j, tmp);
        }
    }
}
