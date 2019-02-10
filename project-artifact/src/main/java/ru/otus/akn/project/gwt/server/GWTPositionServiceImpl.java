package ru.otus.akn.project.gwt.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import ru.otus.akn.project.db.entity.PositionEntity;
import ru.otus.akn.project.ejb.api.stateless.PositionsService;
import ru.otus.akn.project.gwt.client.service.GWTPositionService;
import ru.otus.akn.project.gwt.shared.Position;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/Project/GWTPositionService")
public class GWTPositionServiceImpl extends RemoteServiceServlet implements GWTPositionService {

    @EJB
    private PositionsService positionsService;

    @Override
    public List<Position> getAllPositions() {
        List<PositionEntity> allPositions = positionsService.getAllPositionEntities();
        List<Position> result = new ArrayList<>();
        for (PositionEntity entity : allPositions) {
            result.add(new Position(entity.getPositionName()));
        }
        return result;
    }
}
