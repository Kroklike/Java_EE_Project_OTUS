package ru.otus.akn.project.ejb.services.stateless;

import ru.otus.akn.project.db.api.StatisticDAO;
import ru.otus.akn.project.db.entity.StatisticEntity;
import ru.otus.akn.project.ejb.api.stateless.StatisticService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Stateless
public class StatisticServiceImpl implements StatisticService {

    @EJB
    private StatisticDAO statisticDAO;

    @Override
    public List<StatisticEntity> getAllStatisticEntitiesFromDB() {
        List<StatisticEntity> allStatistic = statisticDAO.getAllStatisticEntities();
        return allStatistic.stream()
                .sorted(Comparator.comparing(StatisticEntity::getVisitDate).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Long> getRoadMapByStatMark(@NotNull String statisticMark) {
        return statisticDAO.getRoadMapByStatMark(statisticMark);
    }

    @Override
    public BigDecimal saveStatisticInfoUsingProcedure(@NotNull StatisticEntity statisticEntity,
                                                      Long previousStatId) {
        return statisticDAO.saveStatisticInfoUsingProcedure(statisticEntity, previousStatId);
    }
}
