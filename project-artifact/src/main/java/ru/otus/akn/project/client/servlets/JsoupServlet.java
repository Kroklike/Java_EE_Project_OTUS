package ru.otus.akn.project.client.servlets;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import static ru.otus.akn.project.util.OutputResultUtil.writeMapToResponse;

@WebServlet("/jsoupRBC")
public class JsoupServlet extends HttpServlet {

    private static final String rbcURL = "https://www.rbc.ru/";
    private static final Map<String, String> NEWS_HEADERS = new LinkedHashMap<>();
    private static final int MAX_NUMBER_OF_NEWS = 10;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
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
                String newsTitle = new String(text.get(0).text().getBytes(), StandardCharsets.UTF_8);
                if (firstNews) {
                    firstNews = false;
                    if (!NEWS_HEADERS.isEmpty() && NEWS_HEADERS.containsKey(newsTitle)) {
                        break;
                    }
                    NEWS_HEADERS.clear();
                }

                NEWS_HEADERS.put(newsTitle, hrefData);
            }
            resp.setCharacterEncoding("UTF-8");
            String callbackParam = req.getParameter("callback");
            if (callbackParam != null) {
                jsonpResponse(resp, callbackParam);
            } else {
                writeMapToResponse(resp, NEWS_HEADERS);
            }
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong when tried to get news from rbc.ru", e);
        }
    }

    private void jsonpResponse(HttpServletResponse resp,
                               String callback) throws JSONException, IOException {
        JSONArray array = new JSONArray();

        for (Map.Entry<String, String> entry : NEWS_HEADERS.entrySet()) {
            JSONObject object = new JSONObject();
            object.append("title", entry.getKey());
            object.append("url", entry.getValue());
            array.put(object);
        }

        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            out.write(callback + "(" + array.toString() + ")");
        }
    }
}
