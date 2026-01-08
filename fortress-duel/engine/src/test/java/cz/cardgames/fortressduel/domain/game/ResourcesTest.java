package cz.cardgames.fortressduel.domain.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourcesTest {

    @Test
    void canAfford_true_onlyWhenEnough() {
        Resources r = new Resources(5, 2, 1);
        assertTrue(r.canAfford(new Resources(5, 0, 1)));
        assertFalse(r.canAfford(new Resources(6, 0, 0)));
        assertFalse(r.canAfford(new Resources(5, 3, 1)));
    }

    @Test
    void pay_subtractsCost_whenAffordable() {
        Resources r = new Resources(5, 2, 1);
        r.pay(new Resources(3, 2, 1));

        assertEquals(2, r.stone());
        assertEquals(0, r.weapons());
        assertEquals(0, r.mana());
    }

    @Test
    void pay_throws_whenNotAffordable() {
        Resources r = new Resources(5, 0, 0);
        assertThrows(IllegalStateException.class, () -> r.pay(new Resources(6, 0, 0)));
    }

    @Test
    void loseUpTo_clampsToZero_and_returnsActuallyRemoved() {
        Resources r = new Resources(5, 2, 0);

        Resources removed = r.loseUpTo(new Resources(10, 1, 3));

        assertEquals(5, removed.stone());
        assertEquals(1, removed.weapons());
        assertEquals(0, removed.mana());

        assertEquals(0, r.stone());
        assertEquals(1, r.weapons());
        assertEquals(0, r.mana());
    }

    @Test
    void stealFromUpTo_movesOnlyWhatVictimHas() {
        Resources attacker = new Resources(0, 0, 0);
        Resources victim = new Resources(4, 0, 0);

        Resources stolen = attacker.stealFromUpTo(victim, new Resources(10, 0, 0));

        assertEquals(4, stolen.stone());
        assertEquals(4, attacker.stone());
        assertEquals(0, victim.stone());
    }
}