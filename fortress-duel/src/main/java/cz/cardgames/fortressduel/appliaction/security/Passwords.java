package cz.cardgames.fortressduel.appliaction.security;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * Password hashing & verification using BCrypt.
 * - Hash format includes version, cost and salt (no need to store salt separately).
 * - Use cost 12 for dev; can be tuned later.
 */
public final class Passwords {
    private static final int COST = 12;

    private Passwords() {}

    /** Hash a plaintext password to a string safe for DB storage. */
    public static String hash(char[] plaintext) {
        // BCrypt automatically generates a random salt
        return BCrypt.withDefaults().hashToString(COST, plaintext);
    }

    /** Verify plaintext password against a stored BCrypt hash. */
    public static boolean verify(char[] plaintext, String hashFromDb) {
        var result = BCrypt.verifyer().verify(plaintext, hashFromDb);
        return result.verified;
    }
}
