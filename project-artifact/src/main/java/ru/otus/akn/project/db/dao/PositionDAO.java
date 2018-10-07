package ru.otus.akn.project.db.dao;

import ru.otus.akn.project.db.entity.PositionEntity;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class PositionDAO {

    public static PositionEntity getPositionEntity(EntityManager em, String positionName) {
        Query positionQ = em.createQuery("select position from PositionEntity position " +
                "where position.positionName = :positionName");
        positionQ.setParameter("positionName", positionName);
        return (PositionEntity) positionQ.getResultList().get(0);
    }

}
