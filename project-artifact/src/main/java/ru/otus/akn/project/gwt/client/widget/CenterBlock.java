package ru.otus.akn.project.gwt.client.widget;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.inject.Inject;

import static ru.otus.akn.project.gwt.client.gin.ApplicationInjector.INSTANCE;

public class CenterBlock extends Composite {
    @UiTemplate("CenterBlock.ui.xml")
    public interface CenterBlockUiBinder extends UiBinder<DeckPanel, CenterBlock> {
    }

    private static CenterBlockUiBinder centerBlockUiBinder = INSTANCE.getCenterBlockUiBinder();

    @UiField
    DeckPanel mainBlock;

    @Inject
    public CenterBlock() {
        initWidget(centerBlockUiBinder.createAndBindUi(this));
        mainBlock.showWidget(0);
    }
}