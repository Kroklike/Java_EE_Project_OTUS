package ru.otus.akn.project.gwt.client.model;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import lombok.Getter;

import java.util.Date;

@Getter
public class NewsItemCreator {

    private final FlowPanel newsItem = new FlowPanel();
    private static final String NEWS_DATE_FORMAT = "dd.MM.yyyy";

    public NewsItemCreator(String newsImageLink, String title, FlowPanel description, Date newsDate) {
        newsItem.addStyleName("news");

        Image descriptionPhoto = new Image(newsImageLink);

        FlowPanel newsInfo = new FlowPanel();
        newsInfo.addStyleName("news-text");
        newsInfo.addStyleName("a-style");

        Label titleItem = new Label();
        titleItem.getElement().setInnerHTML("<h2>" + title + "</h2>");

        Label date = new Label(DateTimeFormat.getFormat(NEWS_DATE_FORMAT).format(newsDate));

        newsInfo.add(titleItem);
        newsInfo.add(description);
        newsInfo.add(date);

        newsItem.add(descriptionPhoto);
        newsItem.add(newsInfo);
    }
}
