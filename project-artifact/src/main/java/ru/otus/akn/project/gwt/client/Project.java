package ru.otus.akn.project.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.ui.RootPanel;
import ru.otus.akn.project.gwt.client.widget.*;
import ru.otus.akn.project.gwt.client.widget.Header;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Entry point classes define <code>onModuleLoad()</code>
 */
public class Project implements EntryPoint {

    private static final String STAT_SERVLET = "http://localhost:8080/gatherStatistic";
    private static final Logger LOGGER = Logger.getLogger(Project.class.getSimpleName());

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        RootPanel.get("header").add(new Header());
        CenterBlock centerBlock = new CenterBlock();
        RootPanel.get("center-block").add(centerBlock);
        RootPanel.get("category").add(new SideMenu());
        RootPanel.get("under-header-menu").add(new UnderHeaderMenu(centerBlock));
        RootPanel.get("footer").add(new Footer());
    }

    public static void fixStat(String pageName) {
        try {
            new RequestBuilder(RequestBuilder.GET, STAT_SERVLET + "?pageName=" + pageName)
                    .sendRequest(null, new RequestCallback() {
                        @Override
                        public void onResponseReceived(Request request, Response response) {
                        }

                        @Override
                        public void onError(Request request, Throwable exception) {
                            LOGGER.log(Level.WARNING, "Cannot fix statistic info", exception);
                        }
                    });
        } catch (RequestException e) {
            LOGGER.log(Level.WARNING, "Something went wrong when tried to fix statistic info", e);
        }
    }
}
