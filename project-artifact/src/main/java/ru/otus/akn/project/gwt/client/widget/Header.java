package ru.otus.akn.project.gwt.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import ru.otus.akn.project.gwt.client.constants.ApplicationConstants;

import javax.inject.Inject;


public class Header extends Composite {
    @UiTemplate("Header.ui.xml")
    interface HeaderUiBinder extends UiBinder<HTMLPanel, Header> {
    }

    private static HeaderUiBinder ourUiBinder = GWT.create(HeaderUiBinder.class);
    private static final ApplicationConstants CONSTANTS = GWT.create(ApplicationConstants.class);

    @UiField(provided = true)
    FormPanel searchPanel = new FormPanel((String) null);
    @UiField(provided = true)
    TextBox searchBox = new DefaultTextBox(CONSTANTS.header_search_box());

    @Inject
    public Header() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}