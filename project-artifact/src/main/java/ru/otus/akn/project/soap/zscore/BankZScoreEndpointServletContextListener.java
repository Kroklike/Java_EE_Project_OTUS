package ru.otus.akn.project.soap.zscore;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.xml.ws.Endpoint;

@WebListener
public class BankZScoreEndpointServletContextListener implements ServletContextListener {

    private Endpoint publishedEndpoint;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String webServiceUrl = "http://localhost:8785" + servletContextEvent.getServletContext().getContextPath() + "/bankZScore";
        publishedEndpoint = Endpoint.publish(webServiceUrl, new BankZScoreService());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        if (publishedEndpoint != null && publishedEndpoint.isPublished()) {
            publishedEndpoint.stop();
        }
    }
}
