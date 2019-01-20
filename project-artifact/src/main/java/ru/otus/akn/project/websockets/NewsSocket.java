package ru.otus.akn.project.websockets;

import org.codehaus.jettison.json.JSONObject;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;

import static ru.otus.akn.project.client.servlets.RBCProcessor.getFreshNewsOrNull;
import static ru.otus.akn.project.util.OutputResultUtil.getJsonResultMap;

@ServerEndpoint(value = "/news")
public class NewsSocket extends UpdatableSocket {

    @OnOpen
    public void onOpen(Session session) {
        super.onOpen(session);
    }

    protected void sendMessageToEverybody(Session session) throws Exception {
        allSessions = session.getOpenSessions();
        Map<String, String> freshNews = getFreshNewsOrNull();
        if (freshNews != null) {
            JSONObject json = getJsonResultMap(freshNews);
            for (Session sess : allSessions) {
                sess.getBasicRemote().sendText(json.toString());
            }
        }
    }

}
