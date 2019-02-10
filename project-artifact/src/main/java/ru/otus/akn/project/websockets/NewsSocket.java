package ru.otus.akn.project.websockets;

import org.codehaus.jettison.json.JSONObject;
import ru.otus.akn.project.ejb.api.singleton.RBCService;

import javax.ejb.EJB;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;

import static ru.otus.akn.project.util.OutputResultUtil.getJsonResultMap;

@ServerEndpoint(value = "/news")
public class NewsSocket extends UpdatableSocket {

    @EJB
    RBCService rbcService;

    @OnOpen
    public void onOpen(Session session) {
        super.onOpen(session);
    }

    protected void sendMessageToEverybody(Session session) throws Exception {
        allSessions = session.getOpenSessions();
        Map<String, String> freshNews = rbcService.getFreshNewsOrNull();
        if (freshNews != null) {
            JSONObject json = getJsonResultMap(freshNews);
            for (Session sess : allSessions) {
                sess.getBasicRemote().sendText(json.toString());
            }
        }
    }

}
