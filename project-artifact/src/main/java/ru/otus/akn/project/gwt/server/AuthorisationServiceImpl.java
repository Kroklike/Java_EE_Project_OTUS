package ru.otus.akn.project.gwt.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import ru.otus.akn.project.gwt.client.service.AuthorisationService;
import ru.otus.akn.project.gwt.shared.User;
import ru.otus.akn.project.gwt.shared.exception.WrongCredentialsException;

import javax.servlet.annotation.WebServlet;

@WebServlet("/Project/AuthorisationService")
public class AuthorisationServiceImpl extends RemoteServiceServlet implements AuthorisationService {

    @Override
    public void authorize(User user) throws WrongCredentialsException {



    }
}
