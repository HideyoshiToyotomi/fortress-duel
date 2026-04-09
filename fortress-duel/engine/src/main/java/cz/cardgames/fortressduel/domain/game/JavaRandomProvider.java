package cz.cardgames.fortressduel.domain.game;

import java.util.Random;

public final class JavaRandomProvider implements RandomProvider {
    private final Random random;

    public JavaRandomProvider() {
        this.random = new Random();
    }

    public JavaRandomProvider(long seed) {
        this.random = new Random(seed);
    }

    @Override
    public int nextInt(int bound) {
        return random.nextInt(bound);
    }

}
