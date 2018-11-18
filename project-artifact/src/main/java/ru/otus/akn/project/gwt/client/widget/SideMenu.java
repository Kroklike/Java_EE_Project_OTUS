package ru.otus.akn.project.gwt.client.widget;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import ru.otus.akn.project.gwt.client.model.NewsJSO;

import static ru.otus.akn.project.gwt.client.gin.ApplicationInjector.INSTANCE;

public class SideMenu extends Composite {

    private static final String NEWS_SERVLET = "http://localhost:8080/jsoupRBC";

    public interface SideMenuUiBinder extends UiBinder<FlowPanel, SideMenu> {
    }

    private static SideMenuUiBinder sideMenuUiBinder = INSTANCE.getSideMenuUiBinder();

    @UiField
    FlowPanel sideMenu;
    @UiField
    FlowPanel news;

    public SideMenu() {
        initWidget(sideMenuUiBinder.createAndBindUi(this));

        new JsonpRequestBuilder()
                .requestObject(NEWS_SERVLET, new AsyncCallback<JsArray<NewsJSO>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                    }

                    @Override
                    public void onSuccess(JsArray<NewsJSO> result) {
                        for (int i = 0; i < result.length(); i++) {
                            NewsJSO newsJSO = result.get(i);
                            Anchor newsAnchor = new Anchor(newsJSO.getTitle());
                            newsAnchor.addStyleName("rbc-news-item");
                            newsAnchor.setHref(newsJSO.getUrl());
                            news.add(newsAnchor);
                        }
                    }
                });
    }
}