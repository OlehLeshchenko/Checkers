package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Comment;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CommentServiceJDBC implements CommentService, ConnectionParamsJDBC{
    public static final String CREATE_STATEMENT = "CREATE TABLE IF NOT EXISTS Comment(id INT PRIMARY KEY, player VARCHAR(32) NOT NULL, game VARCHAR(32) NOT NULL, comment VARCHAR(250) NOT NULL, commented_on TIMESTAMP NOT NULL);";
    public static final String INSERT_STATEMENT = "INSERT INTO Comment (id, player, game, comment, commented_on) VALUES (?, ?, ?, ?, ?);";
    public static final String SELECT_STATEMENT = "SELECT * FROM Comment WHERE game=? ORDER BY commented_on;";
    public static final String DELETE_STATEMENT = "DELETE FROM Comment WHERE true";

    public CommentServiceJDBC() {
        try (var connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD); var statement = connection.prepareStatement(CREATE_STATEMENT)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }
    }

    @Override
    public void addComment(Comment comment) {
        try (var connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             var statement = connection.prepareStatement(INSERT_STATEMENT)) {
            statement.setInt(1, comment.getId());
            statement.setString(2, comment.getPlayer());
            statement.setString(3, comment.getGame());
            statement.setString(4, comment.getComment());
            statement.setTimestamp(5, new Timestamp(comment.getCommentedOn().getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }

    }

    @Override
    public List<Comment> getComments(String game) {
        try (var connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             var statement = connection.prepareStatement(SELECT_STATEMENT)) {
            statement.setString(1, game);
            try (var rs = statement.executeQuery()) {
                var comments = new ArrayList<Comment>();
                while (rs.next())
                    comments.add(new Comment(rs.getString(1), rs.getString(2), rs.getString(3), rs.getTimestamp(4)));
                return comments;
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
