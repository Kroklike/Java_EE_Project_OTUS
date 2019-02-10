package ru.otus.akn.project.ejb.api.stateless;

import ru.otus.akn.project.db.entity.StatisticEntity;

import javax.ejb.Remote;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Remote
public interface StatisticService {

    List<StatisticEntity> getAllStatisticEntitiesFromDB();

    Map<String, Long> getRoadMapByStatMark(@NotNull String statisticMark);

    BigDecimal saveStatisticInfoUsingProcedure(@NotNull StatisticEntity statisticEntity,
                                               Long previousStatId);

}
