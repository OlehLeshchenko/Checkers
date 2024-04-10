package sk.tuke.gamestudio.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import sk.tuke.gamestudio.entity.Save;

import java.util.List;

@Transactional
public class SaveServiceJPA extends SaveService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addSave(Save save) {
        entityManager.persist(save);
    }

    @Override
    public List<Save> getSaves(String game, String player) {
        return entityManager.createQuery("SELECT s FROM Save s WHERE s.game=:game AND s.player=:player ORDER BY s.savedOn", Save.class)
                .setParameter("game", game)
                .setParameter("player", player)
                .getResultList();
    }

    @Override
    public void reset() {
        entityManager.createQuery("DELETE FROM Save").executeUpdate();
    }

}
