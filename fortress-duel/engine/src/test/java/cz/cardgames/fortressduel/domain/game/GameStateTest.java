package cz.cardgames.fortressduel.domain.game;

import cz.cardgames.fortressduel.domain.setup.GameMode;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameStateTest {

    @Test
    void endTurn_produces_and_switches_active_player() {
        PlayerState p1 = new PlayerState(
                "p1", "t1",
                new Resources(0,0,0),
                new Workers(1,0,0),
                30, 10
        );
        PlayerState p2 = new PlayerState(
                "p2", "t2",
                new Resources(0,0,0),
                new Workers(0,1,0),
                30, 10
        );

        GameState game = new GameState(
                GameMode.DUEL_1V1,
                List.of(p1, p2),
                0
        );

        game.endTurn();

        // p1 vyprodukoval
        assertEquals(1, p1.resources().stone());
        // aktivní hráč se přepnul
        assertEquals("p2", game.activePlayer().playerId());

        game.endTurn();

        // p2 vyprodukoval
        assertEquals(1, p2.resources().weapons());
        assertEquals("p1", game.activePlayer().playerId());
    }

    @Test
    void duel_requiresExactlyTwoPlayers() {
        PlayerState p1 = new PlayerState("p1", null, new Resources(0,0,0), new Workers(0,0,0), 30, 10);

        assertThrows(IllegalArgumentException.class, () ->
                new GameState(GameMode.DUEL_1V1, List.of(p1), 0)
        );
    }

    @Test
    void team_requiresExactlyFourPlayers() {
        PlayerState p1 = new PlayerState("p1", "A", new Resources(0,0,0), new Workers(0,0,0), 30, 10);
        PlayerState p2 = new PlayerState("p2", "B", new Resources(0,0,0), new Workers(0,0,0), 30, 10);

        assertThrows(IllegalArgumentException.class, () ->
                new GameState(GameMode.TEAM_2V2, List.of(p1, p2), 0)
        );
    }
}