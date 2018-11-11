package ru.otus.akn.project.gwt.client.widget;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;
import ru.otus.akn.project.gwt.client.constants.ApplicationMessages;

import java.util.Date;

import static ru.otus.akn.project.gwt.client.gin.ApplicationInjector.INSTANCE;

public class Footer extends Composite {
    @UiTemplate("Footer.ui.xml")
    public interface FooterUiBinder extends UiBinder<Label, Footer> {
    }

    private static FooterUiBinder ourUiBinder = INSTANCE.getFooterUiBinder();
    private static final ApplicationMessages MESSAGES = INSTANCE.getMessages();
    private static final String DATE_FORMAT = "yyyy";

    @UiField(provided = true)
    Label footerLabel = new Label(MESSAGES.footerLabelText(DateTimeFormat.getFormat(DATE_FORMAT).format(new Date())));

    @Inject
    public Footer() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}