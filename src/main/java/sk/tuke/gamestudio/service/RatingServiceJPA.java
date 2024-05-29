package sk.tuke.gamestudio.service;

import jakarta.persistence.NoResultException;
import sk.tuke.gamestudio.entity.Rating;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Transactional
public class RatingServiceJPA implements RatingService{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) {
        try {
            Rating existingRating = entityManager.createQuery("SELECT r FROM Rating r WHERE r.player = :player AND r.game = :game", Rating.class)
                    .setParameter("player", rating.getPlayer())
                    .setParameter("game", rating.getGame())
                    .getSingleResult();
            existingRating.setRating(rating.getRating());
            existingRating.setRatedOn(rating.getRatedOn());
        } catch (NoResultException e) {
            entityManager.persist(rating);
        }

    }

    @Override
    public int getRating(String game, String player) {
        Integer result;
        try {
            result = entityManager.createQuery("SELECT r.rating FROM Rating r WHERE r.game=:game AND r.player=:player", Integer.class)
                    .setParameter("game", game)
                    .setParameter("player", player)
                    .getSingleResult();
        } catch (NoResultException e) {
            result = 0;
        }
        return result;
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