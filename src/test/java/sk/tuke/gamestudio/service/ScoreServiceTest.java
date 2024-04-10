package sk.tuke.gamestudio.service;

import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.entity.Score;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScoreServiceTest {
    private final ScoreService scoreService = new ScoreServiceJDBC();

//    @Test
    public void testReset() {
        scoreService.reset();
        assertEquals(0, scoreService.getTopScores("checkers").size());
    }

//    @Test
    public void testAddScore() {
        scoreService.reset();
        var date = new Date();

        scoreService.addScore(new Score("player", "checkers", 100, date));

        var scores = scoreService.getTopScores("checkers");
        assertEquals(1, scores.size());
        assertEquals("checkers", scores.get(0).getGame());
        assertEquals("player", scores.get(0).getPlayer());
        assertEquals(100, scores.get(0).getPoints());
        assertEquals(date, scores.get(0).getPlayedOn());
    }

//    @Test
    public void testGetTopScores() {
        scoreService.reset();
        var date = new Date();
        scoreService.addScore(new Score("player1", "checkers", 120, date));
        scoreService.addScore(new Score("player2", "checkers", 150, date));
        scoreService.addScore(new Score("player3", "dots", 180, date));
        scoreService.addScore(new Score("player4", "checkers", 100, date));

        var scores = scoreService.getTopScores("checkers");

        assertEquals(3, scores.size());

        assertEquals("checkers", scores.get(0).getGame());
        assertEquals("player2", scores.get(0).getPlayer());
        assertEquals(150, scores.get(0).getPoints());
        assertEquals(date, scores.get(0).getPlayedOn());

        assertEquals("checkers", scores.get(1).getGame());
        assertEquals("player1", scores.get(1).getPlayer());
        assertEquals(120, scores.get(1).getPoints());
        assertEquals(date, scores.get(1).getPlayedOn());

        assertEquals("checkers", scores.get(2).getGame());
        assertEquals("player4", scores.get(2).getPlayer());
        assertEquals(100, scores.get(2).getPoints());
        assertEquals(date, scores.get(2).getPlayedOn());
    }
}
