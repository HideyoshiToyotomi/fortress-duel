package cz.cardgames.fortressduel.adapters.persistence.db;

import cz.cardgames.fortressduel.domain.port.PlayerRepository;

import java.sql.ResultSet;

/** H2 file DB implementation of PlayerRepository (simple JDBC). */
public class H2PlayerRepository implements PlayerRepository {
    @Override
    public boolean existsByName(String name) {
        String sql = "SELECT 1 FROM players WHERE name = ? LIMIT 1";
        try (var con = Db.get(); var ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            throw new RuntimeException("existsByName failed", e);
        }
    }

    @Override
    public void create(String playerId, String name, String passHash) {
        String sql = "INSERT INTO players(player_id, name, pass_hash) VALUES (?,?,?)";
        try (var con = Db.get(); var ps = con.prepareStatement(sql)) {
            ps.setString(1, playerId);
            ps.setString(2, name);
            ps.setString(3, passHash);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("create failed", e);
        }
    }

    @Override
    public String findPassHashByName(String name) {
        String sql = "SELECT pass_hash FROM players WHERE name = ? LIMIT 1";
        try (var con = Db.get(); var ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString(1);
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException("findPassHashByName failed", e);
        }
    }
}
