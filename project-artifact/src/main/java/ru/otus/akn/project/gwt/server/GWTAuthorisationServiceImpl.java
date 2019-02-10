package ru.otus.akn.project.gwt.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.apache.commons.codec.digest.DigestUtils;
import ru.otus.akn.project.db.entity.UserEntity;
import ru.otus.akn.project.ejb.api.stateless.UsersService;
import ru.otus.akn.project.gwt.client.service.GWTAuthorisationService;
import ru.otus.akn.project.gwt.shared.User;
import ru.otus.akn.project.gwt.shared.exception.WrongCredentialsException;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;

@WebServlet("/Project/GWTAuthorisationService")
public class GWTAuthorisationServiceImpl extends RemoteServiceServlet implements GWTAuthorisationService {

    @EJB
    private UsersService usersService;

    @Override
    public void authorize(User user) throws WrongCredentialsException {

        if (user.getLogin().isEmpty()) {
            throw new WrongCredentialsException("Login should not be null");
        } else if (user.getPassword().isEmpty()) {
            throw new WrongCredentialsException("Password should not be null");
        }

        UserEntity userEntity = usersService.getUserEntityByLogin(user.getLogin());

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
