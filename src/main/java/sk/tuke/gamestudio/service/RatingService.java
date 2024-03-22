package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

import java.util.List;

public interface RatingService {
    /**
     * sets rating to storage
     * @param rating rating object to be added
     */
    void setRating(Rating rating);

    /**
     * Get all ratings from the storage for the game named <code>game</code>
     * @param game name of the game
     * @return list of ratings
     */
    List<Rating> getRating(String game);

    /**
     * Get average rating from the storage for the game named <code>game</code>
     * @param game name of the game
     * @return average rating
     */
    int getAverageRating(String game);

    /**
     * deletes all ratings in the storage (for all games)
     */
    void reset();
}
