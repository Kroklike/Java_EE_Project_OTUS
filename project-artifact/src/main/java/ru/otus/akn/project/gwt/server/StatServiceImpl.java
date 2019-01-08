package ru.otus.akn.project.gwt.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import ru.otus.akn.project.db.entity.StatisticEntity;
import ru.otus.akn.project.gwt.client.service.StatService;
import ru.otus.akn.project.gwt.shared.Statistic;
import ru.otus.akn.project.util.EntityManagerControlGeneric;

import javax.persistence.EntityManager;
import javax.servlet.annotation.WebServlet;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ru.otus.akn.project.db.dao.StatisticDAO.getAllStatisticEntities;
import static ru.otus.akn.project.util.PersistenceUtil.MANAGER_FACTORY;

@WebServlet("/Project/StatService")
public class StatServiceImpl extends RemoteServiceServlet implements StatService {

    @Override
    public List<Statistic> getStatisticInfo() {

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

        List<Statistic> result = new ArrayList<>();
        for (StatisticEntity entity : allStatistic) {
            result.add(convertStatisticEntityToStatistic(entity));
        }

        this.getThreadLocalRequest().getSession().setAttribute("statList", result);

        return result;
    }

    private Statistic convertStatisticEntityToStatistic(StatisticEntity statisticEntity) {
        Statistic statistic = new Statistic();
        statistic.setPageName(statisticEntity.getPageName());
        statistic.setIpAddress(statisticEntity.getIpAddress());
        statistic.setBrowser(statisticEntity.getBrowserInfo());
        statistic.setCookies(statisticEntity.getCookies());
        statistic.setRequestData(statisticEntity.getRequestedParams());
        statistic.setDate(Date.from(statisticEntity.getVisitDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        return statistic;
    }
}
