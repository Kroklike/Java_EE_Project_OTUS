package ru.otus.akn.project.websockets;

import org.codehaus.jettison.json.JSONObject;
import ru.otus.akn.project.ejb.api.singleton.RatesService;

import javax.ejb.EJB;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.math.BigDecimal;
import java.util.Map;

import static ru.otus.akn.project.util.OutputResultUtil.getJsonResultMap;

@ServerEndpoint(value = "/rates")
public class RatesSocket extends UpdatableSocket {

    @EJB
    RatesService ratesService;

    @OnOpen
    public void onOpen(Session session) {
        super.onOpen(session);
    }

    protected void sendMessageToEverybody(Session session) throws Exception {
        allSessions = session.getOpenSessions();
        Map<String, BigDecimal> freshRates = ratesService.getRatesOrNull();
        if (freshRates != null) {
            JSONObject json = getJsonResultMap(freshRates);
            for (Session sess : allSessions) {
                sess.getBasicRemote().sendText(json.toString());
            }
        }
    }

}
