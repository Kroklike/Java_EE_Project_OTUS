package ru.otus.akn.project.gwt.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.apache.commons.codec.digest.DigestUtils;
import ru.otus.akn.project.db.entity.UserEntity;
import ru.otus.akn.project.gwt.client.service.AuthorisationService;
import ru.otus.akn.project.gwt.shared.User;
import ru.otus.akn.project.gwt.shared.exception.WrongCredentialsException;
import ru.otus.akn.project.util.EntityManagerControlGeneric;

import javax.persistence.EntityManager;
import javax.servlet.annotation.WebServlet;

import static ru.otus.akn.project.db.dao.UsersDAO.getUserEntityByLogin;
import static ru.otus.akn.project.util.PersistenceUtil.MANAGER_FACTORY;

@WebServlet("/Project/AuthorisationService")
public class AuthorisationServiceImpl extends RemoteServiceServlet implements AuthorisationService {

    @Override
    public void authorize(User user) throws WrongCredentialsException {

        if (user.getLogin().isEmpty()) {
            throw new WrongCredentialsException("Login should not be null");
        } else if (user.getPassword().isEmpty()) {
            throw new WrongCredentialsException("Password should not be null");
        }

        UserEntity userEntity;

        try {
            userEntity = new EntityManagerControlGeneric<UserEntity>(MANAGER_FACTORY) {
                @Override
                public UserEntity requestMethod(EntityManager manager) {
                    return getUserEntityByLogin(manager, user.getLogin());
                }
            }.processRequest();
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong when try to get user from db", e);
        }

        if (userEntity == null) {
            throw new WrongCredentialsException("User not found");
        } else if (!userEntity.getIsActive()) {
            throw new WrongCredentialsException("User is blocked");
        } else {
            String inputPassword = DigestUtils.sha256Hex(user.getPassword());
            if (inputPassword.equals(userEntity.getPasswordHash())) {
                System.out.println("Success");
            } else {
                throw new WrongCredentialsException("Wrong credentials");
            }
        }
    }
}
