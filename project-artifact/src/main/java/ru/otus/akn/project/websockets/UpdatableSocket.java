package ru.otus.akn.project.websockets;

import javax.websocket.Session;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static ru.otus.akn.project.util.ParamUtil.getMinutesBetweenChecksForServlet;

public abstract class UpdatableSocket {

    protected final ScheduledExecutorService TIMER = Executors.newSingleThreadScheduledExecutor();
    protected Set<Session> allSessions;
    protected boolean isStarted = false;

    public void onOpen(Session session) {
        allSessions = session.getOpenSessions();

        if (!isStarted && allSessions.size() == 1) {
            isStarted = true;

            TIMER.scheduleAtFixedRate(
                    () -> {
                        try {
                            sendMessageToEverybody(session);
                        } catch (Exception e) {
                            throw new RuntimeException("Something went wrong when tried to send message", e);
                        }
                    }, 0, getMinutesBetweenChecksForServlet().intValue(), TimeUnit.MINUTES);
        }
    }

    protected abstract void sendMessageToEverybody(Session session) throws Exception;

}
