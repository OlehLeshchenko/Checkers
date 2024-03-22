package sk.tuke.gamestudio.service;

import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.entity.Comment;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentServiceTest {
    private final CommentService commentService = new CommentServiceJDBC();

    @Test
    public void testReset() {
        commentService.reset();
        assertEquals(0, commentService.getComments("checkers").size());
    }

    @Test
    public void testAddComment() {
        commentService.reset();
        var date = new Date();

        commentService.addComment(new Comment("player", "checkers", "Good game!", date));

        var scores = commentService.getComments("checkers");
        assertEquals(1, scores.size());
        assertEquals("checkers", scores.get(0).getGame());
        assertEquals("player", scores.get(0).getPlayer());
        assertEquals("Good game!", scores.get(0).getText());
        assertEquals(date, scores.get(0).getCommentedAt());
    }

    @Test
    public void testGetComments() {
        commentService.reset();

        var date = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, 2045);
        var lastDate = calendar.getTime();

        commentService.addComment(new Comment("player1", "checkers", "Good game!", date));
        commentService.addComment(new Comment("player2", "checkers", "The best game ever!!!", lastDate));
        commentService.addComment(new Comment("player3", "dots", "The worst game ever!!!", date));
        commentService.addComment(new Comment("player4", "checkers", "good game", date));

        var scores = commentService.getComments("checkers");

        assertEquals(3, scores.size());

        assertEquals("checkers", scores.get(0).getGame());
        assertEquals("player1", scores.get(0).getPlayer());
        assertEquals("Good game!", scores.get(0).getText());
        assertEquals(date, scores.get(0).getCommentedAt());

        assertEquals("checkers", scores.get(1).getGame());
        assertEquals("player4", scores.get(1).getPlayer());
        assertEquals("good game", scores.get(1).getText());
        assertEquals(date, scores.get(1).getCommentedAt());

        assertEquals("checkers", scores.get(2).getGame());
        assertEquals("player2", scores.get(2).getPlayer());
        assertEquals("The best game ever!!!", scores.get(2).getText());
        assertEquals(lastDate, scores.get(2).getCommentedAt());
    }
}
