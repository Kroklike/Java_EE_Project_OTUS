package ru.otus.akn.project.db.dao;

import ru.otus.akn.project.db.entity.UserEntity;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class UsersDAO {

    public static List<UserEntity> getAllUsersEntities(EntityManager em) {
        Query userQ = em.createQuery("select user from UserEntity user ");
        return (List<UserEntity>) userQ.getResultList();
    }

    public static UserEntity getUserEntityByLogin(EntityManager em, String login) {
        Query userQ = em.createQuery("select user from UserEntity user where " +
                "user.login = :login");
        userQ.setParameter("login", login);
        List<UserEntity> list = userQ.getResultList();
        if (list.size() == 0) {
            return null;
        } else if (list.size() > 1) {
            throw new RuntimeException("Found more than one user");
        }
        return list.get(0);
    }

}
