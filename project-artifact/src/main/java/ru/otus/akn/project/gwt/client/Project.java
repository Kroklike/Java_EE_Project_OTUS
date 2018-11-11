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

        // Assume that the host HTML has elements defined whose
        // IDs are "slot1", "slot2".  In a real app, you probably would not want
        // to hard-code IDs.  Instead, you could, for example, search for all
        // elements with a particular CSS class and replace them with widgets.
        //
        RootPanel.get("header").add(new Header());
        RootPanel.get("under-header-menu").add(new UnderHeaderMenu());
        RootPanel.get("footer").add(new Footer());
        RootPanel.get("center-block").add(new CenterBlock());
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
