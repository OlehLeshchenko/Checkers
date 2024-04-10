package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Transactional
public class RatingServiceJPA implements RatingService{
    private static final String INSERT_STATEMENT = "INSERT INTO Rating (player, game, rating, rated_on) VALUES (?, ?, ?, ?) ON CONFLICT (player, game) DO UPDATE SET rating = EXCLUDED.rating, rated_on = EXCLUDED.rated_on;";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) {
        entityManager.createNativeQuery(INSERT_STATEMENT)
                .setParameter(1, rating.getPlayer())
                .setParameter(2, rating.getGame())
                .setParameter(3, rating.getRating())
                .setParameter(4, rating.getRatedOn())
                .executeUpdate();
    }

    @Override
    public int getRating(String game, String player) {
       return entityManager.createQuery("SELECT r.rating FROM Rating r WHERE r.game=:game AND r.player=:player", Integer.class)
               .setParameter("game", game)
               .setParameter("player", player)
               .getSingleResult();
    }

    @Override
    public int getAverageRating(String game) {
        return entityManager.createQuery("SELECT COALESCE(ROUND(AVG(r.rating)), 0) FROM Rating r WHERE r.game=:game", Double.class)
                .setParameter("game", game)
                .getSingleResult()
                .intValue();
    }

    @Override
    public void reset() {
        entityManager.createQuery("DELETE FROM Rating").executeUpdate();
    }

}