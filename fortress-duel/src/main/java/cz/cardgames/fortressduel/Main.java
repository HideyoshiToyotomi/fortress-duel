package cz.cardgames.fortressduel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        log.info("Fortress Duel is starting...");
        log.debug("Debug example: args.length = {}", args.length);
        System.out.println("(Comparison) println works too, but without levels");


    }
}
