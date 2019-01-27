package ru.otus.akn.project.soap.rate;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.xml.ws.Endpoint;

@WebListener
public class BroadEffectiveExchangeRateContextListener implements ServletContextListener {

    private Endpoint publishedEndpoint;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String webServiceUrl = "http://localhost:8785" + servletContextEvent.getServletContext().getContextPath() + "/broadEffExRate";
        publishedEndpoint = Endpoint.publish(webServiceUrl, new BroadEffectiveExchangeRateService());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        if (publishedEndpoint != null && publishedEndpoint.isPublished()) {
            publishedEndpoint.stop();
        }
    }

}
