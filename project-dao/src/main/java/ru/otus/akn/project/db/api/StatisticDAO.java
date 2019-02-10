package ru.otus.akn.project.db.api;


import ru.otus.akn.project.db.entity.StatisticEntity;

import javax.ejb.Local;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Local
public interface StatisticDAO {

    List<StatisticEntity> getAllStatisticEntities();

    Map<String, Long> getRoadMapByStatMark(@NotNull String statisticMark);

    BigDecimal saveStatisticInfoUsingProcedure(@NotNull StatisticEntity statisticEntity,
                                               Long previousStatId);

}
