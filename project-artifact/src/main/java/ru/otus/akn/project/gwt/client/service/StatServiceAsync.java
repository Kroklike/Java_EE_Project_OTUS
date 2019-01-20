package ru.otus.akn.project.gwt.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface StatServiceAsync {
    void getStatisticInfo(AsyncCallback<Void> async);
}
