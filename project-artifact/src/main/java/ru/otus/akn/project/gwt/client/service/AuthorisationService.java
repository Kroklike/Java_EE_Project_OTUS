package ru.otus.akn.project.gwt.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import ru.otus.akn.project.gwt.shared.User;
import ru.otus.akn.project.gwt.shared.exception.WrongCredentialsException;

@RemoteServiceRelativePath("AuthorisationService")
public interface AuthorisationService extends RemoteService {
    void authorize(User user) throws WrongCredentialsException;
}
