package sk.tuke.gamestudio.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import sk.tuke.gamestudio.entity.User;

@Transactional
public class UserServiceJPA implements UserService{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Boolean addUser(User user) {
        if(isUserExist(user.getNickname(), user.getPassword()))
            return false;
        entityManager.persist(user);
        return true;
    }

    @Override
    public Boolean isUserExist(String nickname, String password) {
        System.out.println(nickname + " + " + password);
        return entityManager.createQuery("SELECT EXISTS (SELECT us FROM User us WHERE us.nickname=:nickname AND us.password=:password)", Boolean.class)
                .setParameter("nickname", nickname)
                .setParameter("password", password)
                .getSingleResult();
    }

    @Override
    public void updatePassword(User user, String newPassword) {

    }

    @Override
    public void reset() {
        entityManager.createQuery("DELETE FROM User").executeUpdate();
    }
}
