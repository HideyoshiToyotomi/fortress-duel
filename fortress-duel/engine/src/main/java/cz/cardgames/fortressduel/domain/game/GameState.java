package cz.cardgames.fortressduel.domain.game;

import cz.cardgames.fortressduel.domain.setup.GameMode;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;


public class GameState {

    private final GameMode gameMode;
    private final List<PlayerState> players;
    private int activePlayerIndex;

    public GameState(GameMode gameMode, List<PlayerState> players, int activePlayerIndex) {
        this.gameMode = Objects.requireNonNull(gameMode);
        this.players = List.copyOf(players);
        if (players.isEmpty()) throw new IllegalArgumentException("Players cannot be empty");

        int expected = switch (gameMode) {
            case DUEL_1V1 -> 2;
            case TEAM_2V2 -> 4;
        };
        if (players.size() != expected) {
            throw new IllegalArgumentException(
                    "Invalid player count for " + gameMode + ": expected " + expected + ", got " + players.size()
            );
        }

        if (gameMode == GameMode.TEAM_2V2) {
            validateTeams(players);
        }

        if (activePlayerIndex < 0 || activePlayerIndex >= players.size()) {
            throw new IllegalArgumentException("Invalid activePlayerIndex");
        }
        this.activePlayerIndex = activePlayerIndex;
    }

    private static void validateTeams(List<PlayerState> players) {
        var counts = players.stream()
                .map(PlayerState::teamId)
                .toList();

        if (counts.stream().anyMatch(t -> t == null || t.isBlank())) {
            throw new IllegalArgumentException("TEAM_2V2 requires non-null, non-blank teamId for all players");
        }

        var byTeam = players.stream()
                .collect(groupingBy(PlayerState::teamId,counting()));

        if (byTeam.size() != 2) {
            throw new IllegalArgumentException("TEAM_2V2 requires exactly 2 teams, got " + byTeam.size() + ": " + byTeam.keySet());
        }

        for (var e : byTeam.entrySet()) {
            if (e.getValue() != 2) {
                throw new IllegalArgumentException("TEAM_2V2 requires 2 players per team. Team " + e.getKey() + " has " + e.getValue());
            }
        }
    }

    public GameMode gameMode() {
        return gameMode;
    }

    public PlayerState activePlayer() {
        return players.get(activePlayerIndex);
    }

    public List<PlayerState> players() {
        return players;
    }

    public void endTurn() {
        activePlayer().produceEndTurn();
        advanceTurn();
    }

    private void advanceTurn() {
        activePlayerIndex = (activePlayerIndex + 1) % players.size();
    }


}
