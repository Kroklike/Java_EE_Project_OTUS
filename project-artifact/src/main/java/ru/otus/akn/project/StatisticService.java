package ru.otus.akn.project;

import ru.otus.akn.project.db.entity.StatisticEntity;
import ru.otus.akn.project.util.EntityManagerControlGeneric;

import javax.persistence.EntityManager;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.otus.akn.project.db.dao.StatisticDAO.getAllStatisticEntities;
import static ru.otus.akn.project.util.PersistenceUtil.MANAGER_FACTORY;

public class StatisticService {

    public static List<StatisticEntity> getAllStatisticEntitiesFromDB() {
        List<StatisticEntity> allStatistic;

        try {
            allStatistic = new EntityManagerControlGeneric<List<StatisticEntity>>(MANAGER_FACTORY) {
                @Override
                public List<StatisticEntity> requestMethod(EntityManager manager) {
                    return getAllStatisticEntities(manager);
                }
            }.processRequest();
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong when tried to get statistic entities from DB.", e);
        }

        return allStatistic.stream()
                .sorted(Comparator.comparing(StatisticEntity::getVisitDate).reversed())
                .collect(Collectors.toList());
    }

    public static List<StatisticEntity> getLastTenStatisticEntitiesFromDB() {
        List<StatisticEntity> entitiesFromDB = getAllStatisticEntitiesFromDB();

        if (entitiesFromDB.size() >= 10) {
            return entitiesFromDB.subList(0, 10);
        } else if (entitiesFromDB.isEmpty()){
            return entitiesFromDB;
        } else {
            return entitiesFromDB.subList(0, entitiesFromDB.size());
        }
    }

}
