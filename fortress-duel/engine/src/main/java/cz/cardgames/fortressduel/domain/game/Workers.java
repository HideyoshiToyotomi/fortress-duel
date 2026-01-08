package cz.cardgames.fortressduel.domain.game;

import java.util.Objects;

public class Workers {
    private int builders;
    private int smiths;
    private int mages;

    public Workers(int builders, int smiths, int mages) {
        if (builders < 0 || smiths < 0 || mages < 0) {
            throw new IllegalArgumentException("Workers cannot be negative");
        }
        this.builders = builders;
        this.smiths = smiths;
        this.mages = mages;
    }

    public int builders() { return builders; }
    public int smiths() { return smiths; }
    public int mages() { return mages; }

    public Resources produce() {
        return new Resources(builders, smiths, mages);
    }

    public void add(Workers delta) {
        Objects.requireNonNull(delta);
        this.builders += delta.builders;
        this.smiths += delta.smiths;
        this.mages += delta.mages;
    }

    public Workers loseUpTo(Workers amount) {
        Objects.requireNonNull(amount);
        if (amount.builders < 0 || amount.smiths < 0 || amount.mages < 0) {
            throw new IllegalArgumentException("Loss amount cannot be negative");
        }

        int takenBuilders = Math.min(this.builders, amount.builders);
        int takenSmiths = Math.min(this.smiths, amount.smiths);
        int takenMages = Math.min(this.mages, amount.mages);

        this.builders -= takenBuilders;
        this.smiths -= takenSmiths;
        this.mages -= takenMages;

        return new Workers(takenBuilders, takenSmiths, takenMages);
    }

    public Workers kidnapFromUpTo(Workers victim, Workers count) {
        Objects.requireNonNull(victim);
        Workers kidnapped = victim.loseUpTo(count);
        this.add(kidnapped);
        return kidnapped;
    }
}
