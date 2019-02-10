package ru.otus.akn.project.db.dao;

import ru.otus.akn.project.db.api.UsersDAO;
import ru.otus.akn.project.db.entity.UserEntity;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.List;

@Stateless
public class UsersDAOImpl implements UsersDAO {

    @PersistenceContext(unitName = "jpa")
    private EntityManager entityManager;

    @Override
    public List<UserEntity> getAllUsersEntities() {
        Query userQ = entityManager.createQuery("select user from UserEntity user ");
        return (List<UserEntity>) userQ.getResultList();
    }

    @Override
    public UserEntity getUserEntityByLogin(@NotNull String login) {
        Query userQ = entityManager.createQuery("select user from UserEntity user where " +
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

    @Override
    public UserEntity getUserEntityById(Long userId) {
        Query userQ = entityManager.createQuery("select user from UserEntity user where " +
                "user.userId = :userId");
        userQ.setParameter("userId", userId);
        List<UserEntity> list = userQ.getResultList();
        if (list.size() == 0) {
            throw new RuntimeException("User not found");
        } else if (list.size() > 1) {
            throw new RuntimeException("Found more than one user");
        }
        return list.get(0);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteAllUsersEntities() {
        Query usersQ = entityManager.createQuery("delete from UserEntity user");
        usersQ.executeUpdate();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveAllUsers(@NotNull List<UserEntity> userEntities) {
        for (UserEntity userEntity : userEntities) {
            if (userEntity.getUserId() != null) {
                userEntity.setUserId(null);
            }
            entityManager.persist(userEntity);
        }
    }
}
