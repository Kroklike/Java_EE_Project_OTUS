package ru.otus.akn.project.gwt.client.gin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import ru.otus.akn.project.gwt.client.constants.ApplicationConstants;
import ru.otus.akn.project.gwt.client.constants.ApplicationMessages;
import ru.otus.akn.project.gwt.client.widget.CenterBlock.CenterBlockUiBinder;
import ru.otus.akn.project.gwt.client.widget.Footer.FooterUiBinder;
import ru.otus.akn.project.gwt.client.widget.Header.HeaderUiBinder;
import ru.otus.akn.project.gwt.client.widget.UnderHeaderMenu.UnderHeaderMenuUiBinder;

@GinModules(ApplicationGinModule.class)
public interface ApplicationInjector extends Ginjector {

    ApplicationInjector INSTANCE = GWT.create(ApplicationInjector.class);

    ApplicationConstants getConstants();

    ApplicationMessages getMessages();

    HeaderUiBinder getHeaderUiBinder();

    FooterUiBinder getFooterUiBinder();

    UnderHeaderMenuUiBinder getUnderHeaderUiBinder();

    CenterBlockUiBinder getCenterBlockUiBinder();
}
