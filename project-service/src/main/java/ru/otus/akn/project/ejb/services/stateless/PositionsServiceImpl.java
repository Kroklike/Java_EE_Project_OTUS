package ru.otus.akn.project.ejb.services.stateless;

import ru.otus.akn.project.db.api.PositionsDAO;
import ru.otus.akn.project.db.entity.PositionEntity;
import ru.otus.akn.project.ejb.api.stateless.PositionsService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.validation.constraints.NotNull;
import java.util.List;

@Stateless
public class PositionsServiceImpl implements PositionsService {

    @EJB
    private PositionsDAO positionsDAO;

    @Override
    public List<PositionEntity> getAllPositionEntities() {
        return positionsDAO.getAllPositionEntities();
    }

    @Override
    public PositionEntity getPositionEntity(@NotNull String positionName) {
        return positionsDAO.getPositionEntity(positionName);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void deleteAllPositionsEntities() {
        positionsDAO.deleteAllPositionsEntities();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void saveAllPositions(@NotNull List<PositionEntity> positionEntities) {
        positionsDAO.saveAllPositions(positionEntities);
    }
}
