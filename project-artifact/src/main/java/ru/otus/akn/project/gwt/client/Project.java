package ru.otus.akn.project.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import ru.otus.akn.project.gwt.client.widget.*;

/**
 * Entry point classes define <code>onModuleLoad()</code>
 */
public class Project implements EntryPoint {

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
}
