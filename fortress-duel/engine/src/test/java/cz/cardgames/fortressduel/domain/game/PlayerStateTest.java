package cz.cardgames.fortressduel.domain.game;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerStateTest {

    @Test
    void produceEndTurn_addsProductionToResources() {
        PlayerState p = new PlayerState(
                "p1", "t1",
                new Resources(0, 0, 0),
                new Workers(2, 1, 0),
                30, 10, new DeckState(List.of(card("x1")))
        );

        p.produceEndTurn();

        assertEquals(2, p.resources().stone());
        assertEquals(1, p.resources().weapons());
        assertEquals(0, p.resources().mana());
    }

    @Test
    void damageWallOnly_doesNotTouchCastle() {
        PlayerState p = new PlayerState(
                "p1", "t1",
                new Resources(0, 0, 0),
                new Workers(0, 0, 0),
                10, 5, new DeckState(List.of(card("x1")))
        );

        p.damageWallOnly(10);

        assertEquals(0, p.wall());
        assertEquals(10, p.castle());
    }

    @Test
    void damageCastleOnly_ignoresWall() {
        PlayerState p = new PlayerState(
                "p1", "t1",
                new Resources(0, 0, 0),
                new Workers(0, 0, 0),
                10, 5, new DeckState(List.of(card("x1")))
        );

        p.damageCastleOnly(3);

        assertEquals(5, p.wall());
        assertEquals(7, p.castle());
    }

    @Test
    void damageWallThenCastle_overflows() {
        PlayerState p = new PlayerState(
                "p1", "t1",
                new Resources(0, 0, 0),
                new Workers(0, 0, 0),
                10, 2, new DeckState(List.of(card("x1")))
        );

        p.damageWallThenCastle(5); // 2 do wall, 3 do castle

        assertEquals(0, p.wall());
        assertEquals(7, p.castle());
    }

    private static cz.cardgames.fortressduel.domain.model.Card card(String id) {
        return new cz.cardgames.fortressduel.domain.model.Card(id, id, cz.cardgames.fortressduel.domain.model.CardType.BUILD);
    }
}