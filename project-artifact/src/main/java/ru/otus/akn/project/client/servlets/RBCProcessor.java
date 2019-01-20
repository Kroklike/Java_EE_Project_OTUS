package ru.otus.akn.project.client.servlets;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class RBCProcessor {

    private static final String rbcURL = "https://www.rbc.ru/";
    private static final Map<String, String> NEWS_HEADERS = new LinkedHashMap<>();
    private static final int MAX_NUMBER_OF_NEWS = 10;

    public static Map<String, String> getFreshNews() throws IOException {
        Map<String, String> result = getFreshNewsOrNull();
        if (result == null) {
            return NEWS_HEADERS;
        } else {
            return result;
        }
    }

    public static Map<String, String> getFreshNewsOrNull() throws IOException {
        boolean firstNews = true;
        Document document = Jsoup.connect(rbcURL).get();
        Elements els = document.getElementsByClass("main-feed__item");
        for (Element el : els.subList(0, Math.min(MAX_NUMBER_OF_NEWS, els.size()))) {
            Elements href = el.getElementsByClass("main-feed__link");
            if (href.size() == 0) {
                continue;
            }
            String hrefData = href.get(0).attr("href");
            Elements text = el.getElementsByClass("main-feed__item__title");
            if (text.size() == 0) {
                continue;
            }
            String newsTitle = text.get(0).text();
            if (firstNews) {
                firstNews = false;
                if (!NEWS_HEADERS.isEmpty() && NEWS_HEADERS.containsKey(new String(newsTitle.getBytes(), UTF_8))) {
                    return null;
                }
                NEWS_HEADERS.clear();
            }

            NEWS_HEADERS.put(newsTitle, hrefData);
        }

        return NEWS_HEADERS;
    }

}
