package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Score;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ScoreServiceJDBC implements ScoreService, ConnectionParamsJDBC {
    public static final String CREATE_STATEMENT = "CREATE TABLE IF NOT EXISTS Score(id INT PRIMARY KEY, player VARCHAR(32) NOT NULL, game VARCHAR(32) NOT NULL, points INT NOT NULL, played_on TIMESTAMP NOT NULL);";
    public static final String INSERT_STATEMENT = "INSERT INTO Score (id, player, game, points, played_on) VALUES (?, ?, ?, ?, ?);";
    public static final String SELECT_STATEMENT = "SELECT * FROM Score WHERE game=? ORDER BY points DESC LIMIT 10;";
    public static final String DELETE_STATEMENT = "DELETE FROM Score WHERE true";

    public ScoreServiceJDBC() {
        try (var connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD); var statement = connection.prepareStatement(CREATE_STATEMENT)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }
    }

    @Override
    public void addScore(Score score) {
        try (var connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             var statement = connection.prepareStatement(INSERT_STATEMENT)) {
            statement.setInt(1, score.getId());
            statement.setString(2, score.getPlayer());
            statement.setString(3, score.getGame());
            statement.setInt(4, score.getPoints());
            statement.setTimestamp(5, new Timestamp(score.getPlayedOn().getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }

    }

    @Override
    public List<Score> getTopScores(String game) {
        try (var connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             var statement = connection.prepareStatement(SELECT_STATEMENT)) {
            statement.setString(1, game);
            try (var rs = statement.executeQuery()) {
                var scores = new ArrayList<Score>();
                while (rs.next())
                    scores.add(new Score(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getTimestamp(4)));
                return scores;
            }
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }

    }

    @Override
    public void reset() {
        try (var connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             var statement = connection.createStatement()) {
            statement.executeUpdate(DELETE_STATEMENT);
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }
    }

}
