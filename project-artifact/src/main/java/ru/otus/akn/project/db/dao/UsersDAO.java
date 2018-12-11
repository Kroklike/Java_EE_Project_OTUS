package ru.otus.akn.project.db.dao;

import ru.otus.akn.project.db.entity.UserEntity;
import ru.otus.akn.project.util.EntityManagerControl;
import ru.otus.akn.project.util.TransactionQueryConsumer;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

import static ru.otus.akn.project.util.PersistenceUtil.MANAGER_FACTORY;

public class UsersDAO {

    public static List<UserEntity> getAllUsersEntities(EntityManager em) {
        Query userQ = em.createQuery("select user from UserEntity user ");
        return (List<UserEntity>) userQ.getResultList();
    }

    public static void deleteAllUsersEntities(EntityManager em) {
        new TransactionQueryConsumer(em) {
            @Override
            public void needToProcessData() {
                Query usersQ = em.createQuery("delete from UserEntity user");
                usersQ.executeUpdate();
            }
        }.processQueryInTransaction();
    }

    public static void saveAllUsers(List<UserEntity> userEntities) throws Exception {
        new EntityManagerControl(MANAGER_FACTORY) {
            @Override
            public void requestMethod(EntityManager manager) {
                new TransactionQueryConsumer(manager) {
                    @Override
                    public void needToProcessData() {
                        for (UserEntity userEntity : userEntities) {
                            if (userEntity.getUserId() != null) {
                                userEntity.setUserId(null);
                            }
                            manager.persist(userEntity);
                        }
                    }
                }.processQueryInTransaction();
            }
        }.processRequest();
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
