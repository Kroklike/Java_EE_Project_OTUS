package ru.otus.akn.project.gwt.client.gin;

import com.google.gwt.inject.client.AbstractGinModule;
import ru.otus.akn.project.gwt.client.constants.ApplicationConstants;
import ru.otus.akn.project.gwt.client.widget.Header.HeaderUiBinder;

public class ApplicationGinModule extends AbstractGinModule {
    protected void configure() {
        bind(ApplicationConstants.class);
        bind(HeaderUiBinder.class);
    }
}
