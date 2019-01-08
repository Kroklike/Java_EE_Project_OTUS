package ru.otus.akn.project.gwt.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import ru.otus.akn.project.gwt.shared.Statistic;

import java.util.List;

public interface StatServiceAsync {
    void getStatisticInfo(AsyncCallback<List<Statistic>> async);
}
