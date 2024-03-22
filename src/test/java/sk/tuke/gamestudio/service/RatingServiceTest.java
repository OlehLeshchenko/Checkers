package sk.tuke.gamestudio.service;

import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.entity.Rating;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RatingServiceTest {
    private final RatingService ratingService = new RatingServiceJDBC();

    @Test
    public void testReset() {
        ratingService.reset();
        assertEquals(0, ratingService.getRating("checkers").size());
    }

    @Test
    public void testAddRating() {
        ratingService.reset();
        var date = new Date();

        ratingService.setRating(new Rating("player", "checkers", 5, date));

        var ratings = ratingService.getRating("checkers");
        assertEquals(1, ratings.size());
        assertEquals("checkers", ratings.get(0).getGame());
        assertEquals("player", ratings.get(0).getPlayer());
        assertEquals(5, ratings.get(0).getRating());
        assertEquals(date, ratings.get(0).getRatedAt());
    }

    @Test
    public void testGetRatings() {
        ratingService.reset();

        var date = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, 2045);
        var lastDate = calendar.getTime();

        ratingService.setRating(new Rating("player1", "checkers", 5, date));
        ratingService.setRating(new Rating("player2", "checkers", 3, lastDate));
        ratingService.setRating(new Rating("player3", "dots", 1, date));
        ratingService.setRating(new Rating("player4", "checkers", 2, date));
        ratingService.setRating(new Rating("player4", "checkers",  4, date));

        var ratings = ratingService.getRating("checkers");

        assertEquals(3, ratings.size());

        assertEquals("checkers", ratings.get(2).getGame());
        assertEquals("player2", ratings.get(2).getPlayer());
        assertEquals(3, ratings.get(2).getRating());
        assertEquals(lastDate, ratings.get(2).getRatedAt());

        assertEquals("checkers", ratings.get(0).getGame());
        assertEquals("player1", ratings.get(0).getPlayer());
        assertEquals(5, ratings.get(0).getRating());
        assertEquals(date, ratings.get(0).getRatedAt());

        assertEquals("checkers", ratings.get(1).getGame());
        assertEquals("player4", ratings.get(1).getPlayer());
        assertEquals(4, ratings.get(1).getRating());
        assertEquals(date, ratings.get(1).getRatedAt());
    }

    @Test
    public void testAverageRating(){
        var date = new Date();
        ratingService.reset();

        ratingService.setRating(new Rating("player1", "checkers", 5, date));
        ratingService.setRating(new Rating("player2", "checkers", 5, date));
        ratingService.setRating(new Rating("player3", "checkers", 1, date));
        ratingService.setRating(new Rating("player4", "checkers", 2, date));

        assertEquals(3, ratingService.getAverageRating("checkers"));
    }
}
