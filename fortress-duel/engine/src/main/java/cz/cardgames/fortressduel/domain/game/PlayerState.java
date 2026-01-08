package cz.cardgames.fortressduel.domain.game;

import java.util.Objects;

public final class PlayerState {
    private final String playerId;
    private final String teamId; // pro 1v1 klidně null nebo např. "A"/"B"
    private final Resources resources;
    private final Workers workers;

    private int castle;
    private int wall;

    public PlayerState(String playerId, String teamId,
                       Resources resources, Workers workers,
                       int castle, int wall) {
        this.playerId = Objects.requireNonNull(playerId);
        this.teamId = teamId; // může být null
        this.resources = Objects.requireNonNull(resources);
        this.workers = Objects.requireNonNull(workers);
        if (castle < 0 || wall < 0) throw new IllegalArgumentException("castle/wall cannot be negative");
        this.castle = castle;
        this.wall = wall;
    }

    public String playerId() { return playerId; }
    public String teamId() { return teamId; }
    public Resources resources() { return resources; }
    public Workers workers() { return workers; }

    public int castle() { return castle; }
    public int wall() { return wall; }

    public void produceEndTurn() {
        resources.add(workers.produce());
    }

    public int damageWallOnly(int amount) {
        if (amount < 0) throw new IllegalArgumentException("amount cannot be negative");
        int taken = Math.min(wall, amount);
        wall -= taken;
        return taken;
    }

    public int damageCastleOnly(int amount) {
        if (amount < 0) throw new IllegalArgumentException("amount cannot be negative");
        int taken = Math.min(castle, amount);
        castle -= taken;
        return taken;
    }

    public int damageWallThenCastle(int amount) {
        if (amount < 0) throw new IllegalArgumentException("amount cannot be negative");
        int remaining = amount - damageWallOnly(amount);
        return damageCastleOnly(remaining);
    }

    public void addToCastle(int amount) {
        if (amount < 0) throw new IllegalArgumentException("amount cannot be negative");
        castle += amount;
    }

    public void addToWall(int amount) {
        if (amount < 0) throw new IllegalArgumentException("amount cannot be negative");
        wall += amount;
    }
}
