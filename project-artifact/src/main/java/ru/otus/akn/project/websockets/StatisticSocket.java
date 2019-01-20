package ru.otus.akn.project.websockets;

import com.google.gson.Gson;
import ru.otus.akn.project.db.entity.StatisticEntity;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.List;

import static ru.otus.akn.project.StatisticService.getAllStatisticEntitiesFromDB;

@ServerEndpoint(value = "/statistic")
public class StatisticSocket extends UpdatableSocket {

    @OnOpen
    public void onOpen(Session session) {
        super.onOpen(session);
    }

    @Override
    protected void sendMessageToEverybody(Session session) throws Exception {
        allSessions = session.getOpenSessions();
        List<StatisticEntity> statistic = getAllStatisticEntitiesFromDB();
        String json = new Gson().toJson(statistic);
        for (Session sess : allSessions) {
            sess.getBasicRemote().sendText(json);
        }
    }
}
