package ru.otus.akn.project.gwt.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import ru.otus.akn.project.gwt.client.ProjectService;

import javax.servlet.annotation.WebServlet;

@WebServlet("/Project/ProjectService")
public class ProjectServiceImpl extends RemoteServiceServlet implements ProjectService {
    // Implementation of sample interface method
    public String getMessage(String msg) {
        return "Client said: \"" + msg + "\"<br>Server answered: \"Hi!\"";
    }
}