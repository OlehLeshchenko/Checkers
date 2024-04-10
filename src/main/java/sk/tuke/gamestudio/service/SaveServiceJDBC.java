package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Save;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SaveServiceJDBC extends SaveService implements ConnectionParamsJDBC {
    public static final String CREATE_STATEMENT = "CREATE TABLE IF NOT EXISTS Save(id INT PRIMARY KEY, player VARCHAR(32) NOT NULL, game VARCHAR(32) NOT NULL, save BYTEA NOT NULL, saved_on TIMESTAMP NOT NULL);";
    public static final String INSERT_STATEMENT = "INSERT INTO Save (id, player, game, save, saved_on) VALUES (?, ?, ?, ?, ?);";
    public static final String SELECT_STATEMENT = "SELECT * FROM Save WHERE game=? AND player=? ORDER BY saved_on;";
    public static final String DELETE_STATEMENT = "DELETE FROM Save WHERE true";

    public SaveServiceJDBC() {
        try (var connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             var statement = connection.prepareStatement(CREATE_STATEMENT)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }
    }

    @Override
    public void addSave(Save save) {
        try (var connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             var statement = connection.prepareStatement(INSERT_STATEMENT)) {
            statement.setInt(1, save.getId());
            statement.setString(2, save.getPlayer());
            statement.setString(3, save.getGame());
            statement.setObject(4, serialize(save.getSave()));
            statement.setTimestamp(5, new Timestamp(save.getSavedOn().getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }
    }

    @Override
    public List<Save> getSaves(String game, String player) {
        try (var connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             var statement = connection.prepareStatement(SELECT_STATEMENT)) {
            statement.setString(1, game);
            statement.setString(2, player);
            try (var rs = statement.executeQuery()) {
                var saves = new ArrayList<Save>();
                while (rs.next()){
                    saves.add(new Save(rs.getString(1), rs.getString(2), deserialize((byte[])rs.getObject(3)), rs.getTimestamp(4)));
                }
                return saves;
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
