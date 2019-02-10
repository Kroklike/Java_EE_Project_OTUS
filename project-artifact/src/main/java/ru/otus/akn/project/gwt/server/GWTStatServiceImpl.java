package ru.otus.akn.project.gwt.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import ru.otus.akn.project.ejb.api.stateless.StatisticService;
import ru.otus.akn.project.gwt.client.service.GWTStatService;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;

@WebServlet("/Project/GWTStatService")
public class GWTStatServiceImpl extends RemoteServiceServlet implements GWTStatService {

    @EJB
    private StatisticService statisticService;

    @Override
    public void getStatisticInfo() {
        this.getThreadLocalRequest().getSession()
                .setAttribute("statList", statisticService.getAllStatisticEntitiesFromDB());
    }

}
