package cz.cardgames.fortressduel.domain.game;

import java.util.Objects;

public final class Resources {
    private int stone;
    private int weapons;
    private int mana;

    public Resources(int stone, int weapons, int mana) {
        if (stone < 0 || weapons < 0 || mana < 0) {
            throw new IllegalArgumentException("Resources cannot be negative");
        }
        this.stone = stone;
        this.weapons = weapons;
        this.mana = mana;
    }

    public int stone() { return stone; }
    public int weapons() { return weapons; }
    public int mana() { return mana; }

    public void add(Resources delta) {
        Objects.requireNonNull(delta);
        this.stone += delta.stone;
        this.weapons += delta.weapons;
        this.mana += delta.mana;
    }

    public boolean canAfford(Resources cost) {
        Objects.requireNonNull(cost);
        if (cost.stone < 0 || cost.weapons < 0 || cost.mana < 0) {
            throw new IllegalArgumentException("Cost cannot be negative");
        }

        return stone >= cost.stone
                && weapons >= cost.weapons
                && mana >= cost.mana;
    }

    public void pay(Resources cost) {
        if (!canAfford(cost)) {
            throw new IllegalStateException("Not enough resources to pay cost");
        }
        this.stone -= cost.stone;
        this.weapons -= cost.weapons;
        this.mana -= cost.mana;
    }

    public Resources loseUpTo(Resources amount) {
        Objects.requireNonNull(amount);
        if (amount.stone < 0 || amount.weapons < 0 || amount.mana < 0) {
            throw new IllegalArgumentException("Loss amount cannot be negative");
        }

        int takenStone = Math.min(this.stone, amount.stone);
        int takenWeapons = Math.min(this.weapons, amount.weapons);
        int takenMana = Math.min(this.mana, amount.mana);

        this.stone -= Math.min(this.stone, amount.stone);
        this.weapons -= Math.min(this.weapons, amount.weapons);
        this.mana -= Math.min(this.mana, amount.mana);

        return new Resources(takenStone, takenWeapons, takenMana);
    }

    public Resources stealFromUpTo(Resources victim, Resources amount) {
        Objects.requireNonNull(victim);
        Resources stolen = victim.loseUpTo(amount);
        this.add(stolen);
        return stolen;
    }

}
