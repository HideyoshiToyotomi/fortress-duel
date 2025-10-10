package cz.cardgames.fortressduel.adapters.persistence.db;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Arrays;

import org.flywaydb.core.Flyway;


/**
 * Minimal DB bootstrap:
 * - uses H2 file database at ~/fortress/data/fortress
 * - creates basic tables if they do not exist
 *
 * H2 JDBC URL cheatsheet:
 *  jdbc:h2:file:/abs/path/fortress         -> absolute path
 *  jdbc:h2:file:~/fortress/data/fortress   -> user home
 * Settings we use:
 *  DB_CLOSE_DELAY=-1   -> keep DB open for JVM lifetime (less reopen overhead)
 *  AUTO_SERVER=TRUE    -> allows re-open from another process (handy for tools)
 */

public final class Db {
    private static final String DB_DIR = System.getProperty("user.home") + "/fortress/data";
    private static final String JDBC_URL =
            "jdbc:h2:file:" + System.getProperty("user.home") + "/fortress/data/fortress" +
                    ";DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE";
    private static final String USER = "sa";     // H2 default
    private static final String PASS = "";       // empty password for local dev

    private Db() {}

    /** Obtain a JDBC connection. Caller should close it (try-with-resources). */
    public static Connection get() throws java.sql.SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASS);
    }

    /** Ensure the folder exists and create basic tables if not present. */
    public static void init() {
        try {
            Files.createDirectories(Path.of(DB_DIR));
            try (Connection con = get(); Statement st = con.createStatement()) {

                Flyway.configure()
                        .dataSource(JDBC_URL, USER, PASS)
                        .load()
                        .migrate();

                var flyway = Flyway.configure().dataSource(JDBC_URL, USER, PASS).load();
                flyway.migrate();
                Arrays.stream(flyway.info().all()).forEach(i ->
                        System.out.println(i.getVersion() + " " + i.getType() + " " + i.getState() + " " + i.getDescription())
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("DB init failed", e);
        }
    }

}
