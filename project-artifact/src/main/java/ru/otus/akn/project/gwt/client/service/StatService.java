package ru.otus.akn.project.gwt.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import ru.otus.akn.project.gwt.shared.Statistic;

import java.util.List;

@RemoteServiceRelativePath("StatService")
public interface StatService extends RemoteService {

    List<Statistic> getStatisticInfo();

}
