package ru.otus.akn.project.db.dao;

import ru.otus.akn.project.db.entity.StatisticEntity;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class StatisticDAO {

    public static List<StatisticEntity> getAllStatisticEntities(EntityManager em) {
        Query statisticQ = em.createQuery("select statistic from StatisticEntity statistic ");
        return (List<StatisticEntity>) statisticQ.getResultList();
    }

}
