package ru.otus.akn.project.gwt.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ProjectServiceAsync {
    void getMessage(String msg, AsyncCallback<String> async);
}
