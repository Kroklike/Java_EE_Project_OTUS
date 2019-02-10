package ru.otus.akn.project.gwt.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("GWTStatService")
public interface GWTStatService extends RemoteService {
    void getStatisticInfo();
}
