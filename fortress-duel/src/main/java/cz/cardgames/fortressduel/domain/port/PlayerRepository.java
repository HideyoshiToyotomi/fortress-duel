package cz.cardgames.fortressduel.domain.port;


/** Storage port for players (DB-backed implementation will come next). */
public interface PlayerRepository {
    /** @return true if name already exists (enforce uniqueness) */
    boolean existsByName(String name);

    /** Create new player row (IDs are strings – generated outside DB) */
    void create(String playerId, String name, String passHash);

    /** @return stored BCrypt hash or null if not found */
    String findPassHashByName(String name);
}
