package sk.tuke.gamestudio.service;

import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.entity.Rating;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RatingServiceTest {
    private final RatingService ratingService = new RatingServiceJDBC();

//    @Test
    public void testReset() {
        ratingService.reset();
        assertEquals(0, ratingService.getAverageRating("checkers"));
    }

//    @Test
    public void testSetRating() {
        ratingService.reset();
        var date = new Date();

        ratingService.setRating(new Rating("player", "checkers", 5, date));
        assertEquals(5, ratingService.getAverageRating("checkers"));
    }

//    @Test
    public void testGetRating() {
        ratingService.reset();

        var date = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, 2045);
        var lastDate = calendar.getTime();

        ratingService.setRating(new Rating("player1", "checkers", 5, date));
        ratingService.setRating(new Rating("player2", "checkers", 3, lastDate));
        ratingService.setRating(new Rating("player4", "checkers", 2, date));
        ratingService.setRating(new Rating("player4", "checkers",  4, date));

        int ratings1 = ratingService.getRating("checkers", "player1");
        int ratings2 = ratingService.getRating("checkers", "player2");
        int ratings4 = ratingService.getRating("checkers", "player4");

        assertEquals(5, ratings1);
        assertEquals(3, ratings2);
        assertEquals(4, ratings4);

    }


//    @Test
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
