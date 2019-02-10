package ru.otus.akn.project.websockets;

import com.google.gson.Gson;
import ru.otus.akn.project.db.entity.StatisticEntity;
import ru.otus.akn.project.ejb.api.stateless.StatisticService;

import javax.ejb.EJB;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.List;

@ServerEndpoint(value = "/statistic")
public class StatisticSocket extends UpdatableSocket {

    @EJB
    private StatisticService statisticService;

    @OnOpen
    public void onOpen(Session session) {
        super.onOpen(session);
    }

    @Override
    protected void sendMessageToEverybody(Session session) throws Exception {
        allSessions = session.getOpenSessions();
        List<StatisticEntity> statistic = statisticService.getAllStatisticEntitiesFromDB();
        String json = new Gson().toJson(statistic);
        for (Session sess : allSessions) {
            sess.getBasicRemote().sendText(json);
        }
    }
}
