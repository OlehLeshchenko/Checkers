package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Save;
import sk.tuke.gamestudio.game.checkers.core.Field;

import java.io.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SaveServiceJDBC implements SaveService {
    public static final String CREATE_STATEMENT = "CREATE TABLE IF NOT EXISTS Save(player VARCHAR(32) NOT NULL, game VARCHAR(32) NOT NULL, save BYTEA NOT NULL, savedOn TIMESTAMP NOT NULL, playedTime INT NOT NULL);";
    public static final String INSERT_STATEMENT = "INSERT INTO Save (player, game, save, savedOn, playedTime) VALUES (?, ?, ?, ?, ?);";
    public static final String SELECT_STATEMENT = "SELECT * FROM Save WHERE game=? ORDER BY savedOn;";
    public static final String DELETE_STATEMENT = "DELETE FROM Save";
    private static final String JDBC_URL = "jdbc:postgresql://localhost/gamestudio";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "iiii";

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
            statement.setString(1, save.getPlayer());
            statement.setString(2, save.getGame());
            statement.setObject(3, serialize(save.getSave()));
            statement.setTimestamp(4, new Timestamp(save.getSavedAt().getTime()));
            statement.setInt(5, save.getPlayedTime());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }
    }

    @Override
    public List<Save> getSaves(String game) {
        try (var connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             var statement = connection.prepareStatement(SELECT_STATEMENT)) {
            statement.setString(1, game);
            try (var rs = statement.executeQuery()) {
                var saves = new ArrayList<Save>();
                while (rs.next()){
                    saves.add(new Save(rs.getString(1), rs.getString(2), deserialize((byte[])rs.getObject(3)), rs.getTimestamp(4), rs.getInt(5)));
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

    @Override
    public byte[] serialize(Field field) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(field);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new GameStudioException(e);
        }
    }

    @Override
    public Field deserialize(byte[] bytes) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (Field) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new GameStudioException(e);
        }
    }
}
