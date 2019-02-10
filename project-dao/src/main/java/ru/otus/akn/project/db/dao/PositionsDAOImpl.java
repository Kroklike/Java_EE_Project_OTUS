package ru.otus.akn.project.db.dao;

import ru.otus.akn.project.db.api.PositionsDAO;
import ru.otus.akn.project.db.entity.DepartmentEntity;
import ru.otus.akn.project.db.entity.PositionEntity;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.List;

@Stateless
public class PositionsDAOImpl implements PositionsDAO {

    @PersistenceContext(unitName = "jpa")
    private EntityManager entityManager;

    @Override
    public List<PositionEntity> getAllPositionEntities() {
        Query positionQ = entityManager.createQuery("select position from PositionEntity position ");
        return (List<PositionEntity>) positionQ.getResultList();
    }

    @Override
    public PositionEntity getPositionEntity(@NotNull String positionName) {
        Query positionQ = entityManager.createQuery("select position from PositionEntity position " +
                "where position.positionName = :positionName");
        positionQ.setParameter("positionName", positionName);
        List<PositionEntity> list = positionQ.getResultList();
        if (list.size() == 0) {
            throw new RuntimeException("Position not found");
        } else if (list.size() > 1) {
            throw new RuntimeException("Found more than one position");
        }
        return list.get(0);
    }

    @Override
    public PositionEntity getPositionEntity(@NotNull Long positionId) {
        Query positionQ = entityManager.createQuery("select position from PositionEntity position where " +
                "position.positionId = :positionId");
        positionQ.setParameter("positionId", positionId);
        List<PositionEntity> list = positionQ.getResultList();
        if (list.size() == 0) {
            throw new RuntimeException("Position not found");
        } else if (list.size() > 1) {
            throw new RuntimeException("Found more than one position");
        }
        return list.get(0);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteAllPositionsEntities() {
        Query positionsQ = entityManager.createQuery("delete from PositionEntity position");
        positionsQ.executeUpdate();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveAllPositions(List<PositionEntity> positionEntities) {
        for (PositionEntity positionEntity : positionEntities) {
            if (positionEntity.getPositionId() != null) {
                positionEntity.setPositionId(null);
            }
            entityManager.persist(positionEntity);
        }
    }
}