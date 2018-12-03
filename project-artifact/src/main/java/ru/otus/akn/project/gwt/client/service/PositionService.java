package ru.otus.akn.project.gwt.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import ru.otus.akn.project.gwt.shared.Position;

import java.util.List;

@RemoteServiceRelativePath("PositionService")
public interface PositionService extends RemoteService {

    List<Position> getAllPositions();

}
