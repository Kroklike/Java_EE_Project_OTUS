package ru.otus.akn.project.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ProjectService")
public interface ProjectService extends RemoteService {
    // Sample interface method of remote interface
    String getMessage(String msg);

    /**
     * Utility/Convenience class.
     * Use ProjectService.App.getInstance() to access static instance of projectServiceAsync
     */
    public static class App {
        private static ProjectServiceAsync ourInstance = GWT.create(ProjectService.class);

        public static synchronized ProjectServiceAsync getInstance() {
            return ourInstance;
        }
    }
}
