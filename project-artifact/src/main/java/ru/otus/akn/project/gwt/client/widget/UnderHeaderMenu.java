package ru.otus.akn.project.gwt.client.widget;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;

import static ru.otus.akn.project.gwt.client.gin.ApplicationInjector.INSTANCE;

public class UnderHeaderMenu extends Composite {
    public interface UnderHeaderMenuUiBinder extends UiBinder<HTMLPanel, UnderHeaderMenu> {
    }

    private static UnderHeaderMenuUiBinder underHeaderMenuUiBinder = INSTANCE.getUnderHeaderUiBinder();

    @Inject
    public UnderHeaderMenu() {
        initWidget(underHeaderMenuUiBinder.createAndBindUi(this));
    }
}