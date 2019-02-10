package ru.otus.akn.project.gwt.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import ru.otus.akn.project.gwt.shared.Position;

import java.util.List;

public interface GWTPositionServiceAsync {
    void getAllPositions(AsyncCallback<List<Position>> async);
}
