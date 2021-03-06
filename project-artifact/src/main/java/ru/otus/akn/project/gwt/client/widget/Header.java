package ru.otus.akn.project.gwt.client.widget;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.inject.Inject;
import ru.otus.akn.project.gwt.client.constants.ApplicationConstants;

import static ru.otus.akn.project.gwt.client.gin.ApplicationInjector.INSTANCE;

public class Header extends Composite {
    @UiTemplate("Header.ui.xml")
    public interface HeaderUiBinder extends UiBinder<HTMLPanel, Header> {
    }

    private static HeaderUiBinder headerUiBinder = INSTANCE.getHeaderUiBinder();
    private static final ApplicationConstants CONSTANTS = INSTANCE.getConstants();

    @UiField(provided = true)
    FormPanel searchPanel = new FormPanel((String) null);
    @UiField(provided = true)
    TextBox searchBox = new DefaultTextBox(CONSTANTS.headerSearchBox());

    @Inject
    public Header() {
        initWidget(headerUiBinder.createAndBindUi(this));
    }
}