package ru.otus.akn.project.db.dao;

import ru.otus.akn.project.db.entity.PositionEntity;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

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

    public static List<PositionEntity> getAllPositionEntities(EntityManager em) {
        Query positionQ = em.createQuery("select position from PositionEntity position ");
        return (List<PositionEntity>) positionQ.getResultList();
    }

}
