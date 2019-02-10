package ru.otus.akn.project.ejb.api.stateless;

import ru.otus.akn.project.db.entity.UserEntity;

import javax.ejb.Remote;
import javax.validation.constraints.NotNull;
import java.util.List;

@Remote
public interface UsersService {

    List<UserEntity> getAllUsersEntities();

    UserEntity getUserEntityByLogin(@NotNull String login);

    void deleteAllUsersEntities();

    void saveAllUsers(@NotNull List<UserEntity> userEntities);

}
