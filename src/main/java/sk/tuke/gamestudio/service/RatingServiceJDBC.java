package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;

public class RatingServiceJDBC implements RatingService, ConnectionParamsJDBC {
    public static final String CREATE_STATEMENT = "CREATE TABLE IF NOT EXISTS Rating(player VARCHAR(32) NOT NULL, game VARCHAR(32) NOT NULL, rating INT NOT NULL, rated_on TIMESTAMP NOT NULL, PRIMARY KEY (player, game));";
    public static final String INSERT_STATEMENT = "INSERT INTO Rating (player, game, rating, rated_on) VALUES (?, ?, ?, ?) ON CONFLICT (player, game) DO UPDATE SET rating = EXCLUDED.rating, rated_on = EXCLUDED.rated_on;";
    public static final String SELECT_RATING_STATEMENT = "SELECT rating FROM Rating WHERE game=? AND player=?;";
    public static final String DELETE_STATEMENT = "DELETE FROM Rating WHERE true";
    public static final String SELECT_AVG_RATING_STATEMENT = "SELECT COALESCE(AVG(Rating.rating), 0) FROM Rating WHERE game = ?";

    public RatingServiceJDBC() {
        try (var connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             var statement = connection.prepareStatement(CREATE_STATEMENT)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }
    }

    @Override
    public void setRating(Rating rating) {
        try (var connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             var statement = connection.prepareStatement(INSERT_STATEMENT)) {
            statement.setString(1, rating.getPlayer());
            statement.setString(2, rating.getGame());
            statement.setInt(3, rating.getRating());
            statement.setTimestamp(4, new Timestamp(rating.getRatedOn().getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }

    }

    @Override
    public int getRating(String game, String player) {
        try (var connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             var statement = connection.prepareStatement(SELECT_RATING_STATEMENT)) {
            statement.setString(1, game);
            statement.setString(2, player);
            try (var rs = statement.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }
    }

    @Override
    public int getAverageRating(String game) {
        try (var connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             var statement = connection.prepareStatement(SELECT_AVG_RATING_STATEMENT)) {
            statement.setString(1, game);
            try (var rs = statement.executeQuery()) {
                rs.next();
                return rs.getInt(1);
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
