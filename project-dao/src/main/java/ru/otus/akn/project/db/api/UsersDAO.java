package ru.otus.akn.project.db.api;

import ru.otus.akn.project.db.entity.UserEntity;

import javax.ejb.Local;
import javax.validation.constraints.NotNull;
import java.util.List;

@Local
public interface UsersDAO {

    List<UserEntity> getAllUsersEntities();

    UserEntity getUserEntityByLogin(@NotNull String login);

    UserEntity getUserEntityById(@NotNull Long userId);

    void deleteAllUsersEntities();

    void saveAllUsers(@NotNull List<UserEntity> userEntities);

}
