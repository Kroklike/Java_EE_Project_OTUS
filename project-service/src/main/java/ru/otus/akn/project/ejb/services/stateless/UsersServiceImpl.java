package ru.otus.akn.project.ejb.services.stateless;

import ru.otus.akn.project.db.api.UsersDAO;
import ru.otus.akn.project.db.entity.UserEntity;
import ru.otus.akn.project.ejb.api.stateless.UsersService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.validation.constraints.NotNull;
import java.util.List;

@Stateless
public class UsersServiceImpl implements UsersService {

    @EJB
    private UsersDAO usersDAO;

    @Override
    public List<UserEntity> getAllUsersEntities() {
        return usersDAO.getAllUsersEntities();
    }

    @Override
    public UserEntity getUserEntityByLogin(@NotNull String login) {
        return usersDAO.getUserEntityByLogin(login);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void saveAllUsers(@NotNull List<UserEntity> userEntities) {
        usersDAO.saveAllUsers(userEntities);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void deleteAllUsersEntities() {
        usersDAO.deleteAllUsersEntities();
    }
}
