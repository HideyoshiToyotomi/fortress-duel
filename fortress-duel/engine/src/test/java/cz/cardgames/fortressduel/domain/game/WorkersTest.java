package cz.cardgames.fortressduel.domain.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorkersTest {

    @Test
    void produce_returnsResourcesMatchingWorkers() {
        Workers w = new Workers(2, 1, 0);
        Resources produced = w.produce();

        assertEquals(2, produced.stone());
        assertEquals(1, produced.weapons());
        assertEquals(0, produced.mana());
    }

    @Test
    void loseUpTo_clampsToZero_and_returnsActuallyRemoved() {
        Workers w = new Workers(1, 0, 2);

        Workers removed = w.loseUpTo(new Workers(5, 3, 1));

        assertEquals(1, removed.builders());
        assertEquals(0, removed.smiths());
        assertEquals(1, removed.mages());

        assertEquals(0, w.builders());
        assertEquals(0, w.smiths());
        assertEquals(1, w.mages());
    }

    @Test
    void kidnapFromUpTo_movesOnlyWhatVictimHas() {
        Workers attacker = new Workers(0, 0, 0);
        Workers victim = new Workers(1, 0, 0);

        Workers kidnapped = attacker.kidnapFromUpTo(victim, new Workers(5, 0, 0));

        assertEquals(1, kidnapped.builders());
        assertEquals(1, attacker.builders());
        assertEquals(0, victim.builders());
    }
}