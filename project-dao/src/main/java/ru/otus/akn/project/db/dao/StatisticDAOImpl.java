package ru.otus.akn.project.db.dao;

import ru.otus.akn.project.db.api.StatisticDAO;
import ru.otus.akn.project.db.entity.StatisticEntity;

import javax.ejb.Stateless;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class StatisticDAOImpl implements StatisticDAO {

    private static final String SAVE_STATISTIC_INFO_PROCEDURE = "save_statistic_info";
    private static final String GET_STATISTIC_ROAD_MAP_PROCEDURE = "get_statistic_road_map";

    @PersistenceContext(unitName = "jpa")
    private EntityManager entityManager;

    @Override
    public List<StatisticEntity> getAllStatisticEntities() {
        Query statisticQ = entityManager.createQuery("select statistic from StatisticEntity statistic ");
        return (List<StatisticEntity>) statisticQ.getResultList();
    }

    @Override
    public Map<String, Long> getRoadMapByStatMark(String statisticMark) {
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery(GET_STATISTIC_ROAD_MAP_PROCEDURE)
                .registerStoredProcedureParameter("p_stat_mark", String.class, ParameterMode.IN)
                .setParameter("p_stat_mark", statisticMark)
                .registerStoredProcedureParameter("stat_road_map", Class.class,
                        ParameterMode.REF_CURSOR);
        query.execute();

        List<Object[]> roadMap = query.getResultList();
        Map<String, Long> result = new HashMap<>();

        for (Object[] map : roadMap) {
            result.put(String.valueOf(map[0]), ((BigDecimal) map[1]).longValue());
        }

        return result;
    }

    @Override
    public BigDecimal saveStatisticInfoUsingProcedure(@NotNull StatisticEntity statisticEntity,
                                                      Long previousStatId) {
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery(SAVE_STATISTIC_INFO_PROCEDURE)
                .registerStoredProcedureParameter("p_stat_mark", String.class, ParameterMode.IN)
                .setParameter("p_stat_mark", statisticEntity.getStatisticMark())
                .registerStoredProcedureParameter("p_page_name", String.class, ParameterMode.IN)
                .setParameter("p_page_name", statisticEntity.getPageName())
                .registerStoredProcedureParameter("p_ip_address", String.class, ParameterMode.IN)
                .setParameter("p_ip_address", statisticEntity.getIpAddress())
                .registerStoredProcedureParameter("p_browser_info", String.class, ParameterMode.IN)
                .setParameter("p_browser_info", statisticEntity.getBrowserInfo())
                .registerStoredProcedureParameter("p_visit_date", LocalDate.class, ParameterMode.IN)
                .setParameter("p_visit_date", statisticEntity.getVisitDate())
                .registerStoredProcedureParameter("p_cookies", String.class, ParameterMode.IN)
                .setParameter("p_cookies", statisticEntity.getCookies())
                .registerStoredProcedureParameter("p_req_params", String.class, ParameterMode.IN)
                .setParameter("p_req_params", statisticEntity.getRequestedParams())
                .registerStoredProcedureParameter("p_prev_stat_id", Long.class, ParameterMode.IN)
                .setParameter("p_prev_stat_id", previousStatId)
                .registerStoredProcedureParameter("ref_result", Class.class, ParameterMode.REF_CURSOR);
        query.execute();
        return (BigDecimal) query.getSingleResult();
    }
}
