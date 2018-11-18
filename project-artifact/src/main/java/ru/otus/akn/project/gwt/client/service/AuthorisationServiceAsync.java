package ru.otus.akn.project.gwt.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import ru.otus.akn.project.gwt.shared.User;

public interface AuthorisationServiceAsync {
    void authorize(User user, AsyncCallback<Void> async);
}
