package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RatingServiceJDBC implements RatingService {
    public static final String CREATE_STATEMENT = "CREATE TABLE IF NOT EXISTS Rating(player VARCHAR(32) NOT NULL, game VARCHAR(32) NOT NULL, rating INT NOT NULL, ratedOn TIMESTAMP NOT NULL, PRIMARY KEY (player, game));";
    public static final String INSERT_STATEMENT = "INSERT INTO Rating (player, game, rating, ratedOn) VALUES (?, ?, ?, ?);";
    public static final String SELECT_STATEMENT = "SELECT * FROM Rating WHERE game=? ORDER BY ratedOn;";
    public static final String UPDATE_STATEMENT = "UPDATE Rating SET rating = ?, ratedOn = ? WHERE player = ? AND game = ?;";
    public static final String DELETE_STATEMENT = "DELETE FROM Rating";
    private static final String JDBC_URL = "jdbc:postgresql://localhost/gamestudio";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "iiii";

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
             var statement = connection.prepareStatement(SELECT_STATEMENT)) {
            statement.setString(1, rating.getGame());
            try (var rs = statement.executeQuery()) {
                var ratings = new ArrayList<Rating>();
                while (rs.next())
                    ratings.add(new Rating(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getTimestamp(4)));
                if(ratings.stream().anyMatch(rating1 -> rating1.getPlayer().equals(rating.getPlayer()))){
                    updateRating(connection, rating);
                } else addRating(connection, rating);
            }
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }

    }

    @Override
    public List<Rating> getRating(String game) {
        try (var connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             var statement = connection.prepareStatement(SELECT_STATEMENT)) {
            statement.setString(1, game);
            try (var rs = statement.executeQuery()) {
                var ratings = new ArrayList<Rating>();
                while (rs.next())
                    ratings.add(new Rating(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getTimestamp(4)));
                return ratings;
            }
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }
    }

    @Override
    public int getAverageRating(String game) {
        var rating = getRating(game).stream().map(Rating::getRating).toList();

        int sum = 0;
        for (Integer i : rating) sum += i;

        return Math.floorDiv(sum, rating.size());
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

    private void updateRating(Connection connection, Rating rating) {
        try (var statement = connection.prepareStatement(UPDATE_STATEMENT)) {
            statement.setInt(1, rating.getRating());
            statement.setTimestamp(2, new Timestamp(rating.getRatedAt().getTime()) );
            statement.setString(3, rating.getPlayer());
            statement.setString(4, rating.getGame());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }
    }

    private void addRating(Connection connection, Rating rating) {
        try (var statement = connection.prepareStatement(INSERT_STATEMENT)) {
            statement.setString(1, rating.getPlayer());
            statement.setString(2, rating.getGame());
            statement.setInt(3, rating.getRating());
            statement.setTimestamp(4, new Timestamp(rating.getRatedAt().getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }
    }

}
