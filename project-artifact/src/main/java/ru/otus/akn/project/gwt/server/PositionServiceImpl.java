package ru.otus.akn.project.gwt.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import ru.otus.akn.project.db.entity.PositionEntity;
import ru.otus.akn.project.gwt.client.service.PositionService;
import ru.otus.akn.project.gwt.shared.Position;
import ru.otus.akn.project.util.EntityManagerControlGeneric;

import javax.persistence.EntityManager;
import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.List;

import static ru.otus.akn.project.db.dao.PositionsDAO.getAllPositionEntities;
import static ru.otus.akn.project.util.PersistenceUtil.MANAGER_FACTORY;

@WebServlet("/Project/PositionService")
public class PositionServiceImpl extends RemoteServiceServlet implements PositionService {

    @Override
    public List<Position> getAllPositions() {
        List<PositionEntity> allPositions;

        try {
            allPositions = new EntityManagerControlGeneric<List<PositionEntity>>(MANAGER_FACTORY) {
                @Override
                public List<PositionEntity> requestMethod(EntityManager manager) {
                    return getAllPositionEntities(manager);
                }
            }.processRequest();
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong when tried to get position entities from DB.", e);
        }

        List<Position> result = new ArrayList<>();
        for (PositionEntity entity : allPositions) {
            result.add(new Position(entity.getPositionName()));
        }

        return result;
    }
}
