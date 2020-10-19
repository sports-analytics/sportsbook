package com.sportsbook.sportsbook.dao;

import com.sportsbook.sportsbook.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("playerDao")
public class PlayerDataAccessService implements PlayerDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PlayerDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertPlayer(UUID playerId, Player player) {
        final String sql = "INSERT INTO player " +
                "(playerId, teamId, firstName, lastName, position, height, weight, age)" +
                " values (?, ?, ?, ?, ?, ?, ?, ?);";

        return jdbcTemplate.update(sql,
                playerId,
                player.getTeamId(),
                player.getFirstName(),
                player.getLastName(),
                player.getPosition(),
                player.getHeight(),
                player.getWeight(),
                player.getAge());
    }

    @Override
    public List<Player> selectAllPlayers() {
        final String sql = "SELECT * from player;";
        return jdbcTemplate.query(sql, (resultSet, i) -> {
            UUID playerId = UUID.fromString(resultSet.getString("playerId"));
            UUID teamId = UUID.fromString(resultSet.getString("teamId"));

            return new Player(
                    playerId,
                    teamId,
                    resultSet.getString("firstName"),
                    resultSet.getString("lastName"),
                    resultSet.getString("position"),
                    resultSet.getString("height"),
                    resultSet.getString("weight"),
                    resultSet.getInt("age"));
        });
    }

    @Override
    public Optional<Player> selectPlayerById(UUID playerId) {
        final String sql = "SELECT * FROM player WHERE playerId = ?;";
        Player player =  jdbcTemplate.queryForObject(sql, new Object[]{playerId}, (resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("playerId"));
            UUID teamId = UUID.fromString(resultSet.getString("teamId"));

            return new Player(
                    id,
                    teamId,
                    resultSet.getString("firstName"),
                    resultSet.getString("lastName"),
                    resultSet.getString("position"),
                    resultSet.getString("height"),
                    resultSet.getString("weight"),
                    resultSet.getInt("age"));
        });
        return Optional.ofNullable(player);
    }

    @Override
    public int deletePlayerById(UUID playerId) {
        final String sql = "DELETE FROM player WHERE playerId = ?;";
        return jdbcTemplate.update(sql, playerId);
    }

    @Override
    public int updatePlayerById(UUID playerId, Player player) {
        final String sql = "UPDATE player SET "
                + "firstName = ?, lastName = ?, position = ?, height = ?, weight = ?, age = ? "
                + "WHERE playerId = ?;";

        return jdbcTemplate.update(sql,
                player.getFirstName(),
                player.getLastName(),
                player.getPosition(),
                player.getHeight(),
                player.getWeight(),
                player.getAge(),
                playerId);
    }
}
