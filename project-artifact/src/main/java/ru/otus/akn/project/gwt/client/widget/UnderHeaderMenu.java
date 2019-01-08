package ru.otus.akn.project.gwt.client.widget;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;

import static ru.otus.akn.project.gwt.client.Project.fixStat;
import static ru.otus.akn.project.gwt.client.gin.ApplicationInjector.INSTANCE;
import static ru.otus.akn.project.gwt.client.widget.CenterBlock.*;

public class UnderHeaderMenu extends Composite {
    public interface UnderHeaderMenuUiBinder extends UiBinder<HTMLPanel, UnderHeaderMenu> {
    }

    private static UnderHeaderMenuUiBinder underHeaderMenuUiBinder = INSTANCE.getUnderHeaderUiBinder();

    @UiField
    Anchor main;
    @UiField
    Anchor entrance;
    @UiField
    Anchor materials;
    @UiField
    Anchor prices;
    @UiField
    Anchor projects;

    @Inject
    public UnderHeaderMenu(CenterBlock centerBlock) {
        initWidget(underHeaderMenuUiBinder.createAndBindUi(this));
        main.addClickHandler(event -> {
            fixStat(MAIN_PAGE_NAME);
            centerBlock.mainBlock.showWidget(MAIN_LINK_INDEX);
        });
        entrance.addClickHandler(event -> {
            fixStat(ENTRANCE_PAGE_NAME);
            centerBlock.mainBlock.showWidget(ENTRANCE_LINK_INDEX);
        });
        materials.addClickHandler(event -> {
            fixStat(MATERIAL_PAGE_NAME);
            centerBlock.mainBlock.showWidget(MATERIAL_LINK_INDEX);
        });
        prices.addClickHandler(event -> {
            fixStat(PRICES_PAGE_NAME);
            centerBlock.mainBlock.showWidget(PRICES_LINK_INDEX);
        });
        projects.addClickHandler(event -> {
            fixStat(PROJECTS_PAGE_NAME);
            centerBlock.mainBlock.showWidget(PROJECTS_LINK_INDEX);
        });
    }
}