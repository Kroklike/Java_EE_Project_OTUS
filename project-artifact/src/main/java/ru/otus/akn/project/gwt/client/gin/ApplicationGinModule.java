package ru.otus.akn.project.gwt.client.gin;

import com.google.gwt.inject.client.AbstractGinModule;
import ru.otus.akn.project.gwt.client.constants.ApplicationConstants;
import ru.otus.akn.project.gwt.client.constants.ApplicationMessages;
import ru.otus.akn.project.gwt.client.service.AuthorisationService;
import ru.otus.akn.project.gwt.client.service.EmployeeService;
import ru.otus.akn.project.gwt.client.widget.CenterBlock.CenterBlockUiBinder;
import ru.otus.akn.project.gwt.client.widget.Footer.FooterUiBinder;
import ru.otus.akn.project.gwt.client.widget.Header.HeaderUiBinder;
import ru.otus.akn.project.gwt.client.widget.SideMenu.SideMenuUiBinder;
import ru.otus.akn.project.gwt.client.widget.UnderHeaderMenu.UnderHeaderMenuUiBinder;

public class ApplicationGinModule extends AbstractGinModule {
    protected void configure() {
        bind(ApplicationConstants.class);
        bind(ApplicationMessages.class);
        bind(HeaderUiBinder.class);
        bind(FooterUiBinder.class);
        bind(UnderHeaderMenuUiBinder.class);
        bind(CenterBlockUiBinder.class);
        bind(AuthorisationService.class);
        bind(SideMenuUiBinder.class);
        bind(EmployeeService.class);
    }
}
