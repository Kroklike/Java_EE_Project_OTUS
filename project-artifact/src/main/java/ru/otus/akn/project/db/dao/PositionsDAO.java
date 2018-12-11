package ru.otus.akn.project.db.dao;

import ru.otus.akn.project.db.entity.PositionEntity;
import ru.otus.akn.project.util.EntityManagerControl;
import ru.otus.akn.project.util.TransactionQueryConsumer;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

import static ru.otus.akn.project.util.PersistenceUtil.MANAGER_FACTORY;

public class PositionsDAO {

    public static PositionEntity getPositionEntity(EntityManager em, String positionName) {
        Query positionQ = em.createQuery("select position from PositionEntity position " +
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

    public static void deleteAllPositionsEntities(EntityManager em) {
        new TransactionQueryConsumer(em) {
            @Override
            public void needToProcessData() {
                Query positionsQ = em.createQuery("delete from PositionEntity position");
                positionsQ.executeUpdate();
            }
        }.processQueryInTransaction();
    }

    public static void saveAllPositions(List<PositionEntity> positionEntities) throws Exception {
        new EntityManagerControl(MANAGER_FACTORY) {
            @Override
            public void requestMethod(EntityManager manager) {
                new TransactionQueryConsumer(manager) {
                    @Override
                    public void needToProcessData() {
                        for (PositionEntity positionEntity : positionEntities) {
                            manager.persist(positionEntity);
                        }
                    }
                }.processQueryInTransaction();
            }
        }.processRequest();
    }

    public static List<PositionEntity> getAllPositionEntities(EntityManager em) {
        Query positionQ = em.createQuery("select position from PositionEntity position ");
        return (List<PositionEntity>) positionQ.getResultList();
    }

}
