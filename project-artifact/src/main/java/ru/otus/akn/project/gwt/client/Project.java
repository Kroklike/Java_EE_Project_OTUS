package ru.otus.akn.project.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import ru.otus.akn.project.gwt.client.widget.CenterBlock;
import ru.otus.akn.project.gwt.client.widget.Footer;
import ru.otus.akn.project.gwt.client.widget.Header;
import ru.otus.akn.project.gwt.client.widget.UnderHeaderMenu;

/**
 * Entry point classes define <code>onModuleLoad()</code>
 */
public class Project implements EntryPoint {

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        final Button button = new Button("Click me");
        final Label label = new Label();

        button.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (label.getText().equals("")) {
                    ProjectService.App.getInstance().getMessage("Hello, World!", new MyAsyncCallback(label));
                } else {
                    label.setText("");
                }
            }
        });

        RootPanel.get("header").add(new Header());
        CenterBlock centerBlock = new CenterBlock();
        RootPanel.get("center-block").add(centerBlock);
        RootPanel.get("under-header-menu").add(new UnderHeaderMenu(centerBlock));
        RootPanel.get("footer").add(new Footer());
    }

    private static class MyAsyncCallback implements AsyncCallback<String> {
        private Label label;

        public MyAsyncCallback(Label label) {
            this.label = label;
        }

        public void onSuccess(String result) {
            label.getElement().setInnerHTML(result);
        }

        public void onFailure(Throwable throwable) {
            label.setText("Failed to receive answer from server!");
        }
    }
}
