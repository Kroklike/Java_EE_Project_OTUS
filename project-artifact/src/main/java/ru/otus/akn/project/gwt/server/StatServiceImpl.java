package ru.otus.akn.project.gwt.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import ru.otus.akn.project.gwt.client.service.StatService;

import javax.servlet.annotation.WebServlet;

import static ru.otus.akn.project.StatisticService.getAllStatisticEntitiesFromDB;

@WebServlet("/Project/StatService")
public class StatServiceImpl extends RemoteServiceServlet implements StatService {

    @Override
    public void getStatisticInfo() {
        this.getThreadLocalRequest().getSession().setAttribute("statList", getAllStatisticEntitiesFromDB());
    }

}
