package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

public interface RatingService {
    /**
     * sets rating to storage
     * @param rating rating object to be added
     */
    void setRating(Rating rating);

    /**
     * Get rating from the storage for the game named <code>game</code> and player
     * @param game name of the game
     * @param player name of the player
     * @return player game rating
     */
    int getRating(String game, String player);

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
