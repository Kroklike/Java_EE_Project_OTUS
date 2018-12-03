package ru.otus.akn.project.gwt.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import ru.otus.akn.project.gwt.shared.Position;

import java.util.List;

public interface PositionServiceAsync {
    void getAllPositions(AsyncCallback<List<Position>> async);
}
