package ru.otus.akn.project.gwt.client.widget;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.http.client.*;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.typedarrays.shared.ArrayBuffer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import org.realityforge.gwt.websockets.client.WebSocket;
import org.realityforge.gwt.websockets.client.WebSocketListener;
import ru.otus.akn.project.gwt.client.constants.ApplicationConstants;
import ru.otus.akn.project.gwt.client.model.NewsJSO;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ru.otus.akn.project.gwt.client.gin.ApplicationInjector.INSTANCE;

public class SideMenu extends Composite {

    private static final Logger LOGGER = Logger.getLogger(SideMenu.class.getSimpleName());
    private static final String USD_KEY = "USD";
    private static final String EUR_KEY = "EUR";

    private static final String NEWS_SERVLET = "http://localhost:8080/jsoupRBC";
    private static final String RATES_URL = "https://www.cbr-xml-daily.ru/daily_utf8.xml";

    public interface SideMenuUiBinder extends UiBinder<FlowPanel, SideMenu> {
    }

    private static SideMenuUiBinder sideMenuUiBinder = INSTANCE.getSideMenuUiBinder();
    private static final ApplicationConstants CONSTANTS = INSTANCE.getConstants();

    @UiField
    FlowPanel sideMenu;
    @UiField
    FlowPanel news;
    @UiField
    FlowPanel currency;

    public SideMenu() {
        initWidget(sideMenuUiBinder.createAndBindUi(this));

        try {
            new RequestBuilder(RequestBuilder.GET, RATES_URL).sendRequest("", new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() == 200) {
                        Document messageDom = XMLParser.parse(response.getText());
                        Map<String, BigDecimal> result = new HashMap<>();
                        NodeList valuteValues = messageDom.getChildNodes();
                        if (valuteValues.getLength() > 0) {
                            valuteValues = valuteValues.item(0).getChildNodes();
                        } else {
                            return;
                        }
                        List<String> currencyForSearch = Arrays.asList(EUR_KEY, USD_KEY);
                        for (int i = 0; i < valuteValues.getLength(); i++) {
                            NodeList childNodes = valuteValues.item(i).getChildNodes();
                            boolean needToAdd = false;
                            String curName = null;
                            BigDecimal curRate = null;
                            for (int j = 0; j < childNodes.getLength(); j++) {
                                Node childItem = childNodes.item(j);
                                if (childItem.getNodeName().equals("CharCode")) {
                                    String charCodeValue = childItem.getFirstChild().getNodeValue();
                                    if (currencyForSearch.contains(charCodeValue)) {
                                        needToAdd = true;
                                        curName = charCodeValue;
                                    }
                                }
                                if (childItem.getNodeName().equals("Value")) {
                                    String rateValue = childItem.getFirstChild().getNodeValue()
                                            .replace("\"", "")
                                            .replace(",", ".");
                                    curRate = new BigDecimal(rateValue);
                                }
                            }
                            if (needToAdd) {
                                result.put(curName, curRate);
                            }
                        }
                        if (result.size() > 0) {
                            currency.clear();
                            currency.add(new Label(CONSTANTS.sideMenuRates()));
                            String currencyInfo = "EUR " + result.get(EUR_KEY) +
                                    " | USD " + result.get(USD_KEY);
                            currency.add(new Label(currencyInfo));
                        }
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    LOGGER.log(Level.WARNING, "Cannot get currency rates", exception);
                }
            });
        } catch (RequestException e) {
            LOGGER.log(Level.WARNING, "Something went wrong when tried to get currency rates", e);
        }

        final WebSocket newsSocket = WebSocket.newWebSocketIfSupported();
        if (newsSocket != null) {
            newsSocket.setListener(new WebSocketListener() {
                @Override
                public void onOpen(@Nonnull WebSocket webSocket) {
                }

                @Override
                public void onClose(@Nonnull WebSocket webSocket, boolean b, int i, @Nullable String s) {
                }

                @Override
                public void onMessage(@Nonnull WebSocket webSocket, @Nonnull String s) {
                    try {
                        JSONObject resultMapNews = new JSONObject(JsonUtils.safeEval(s));
                        news.clear();
                        for (String title : resultMapNews.keySet()) {
                            Anchor newsAnchor = new Anchor(title);
                            newsAnchor.addStyleName("rbc-news-item-gwt");
                            newsAnchor.setHref(getValueFromResult(resultMapNews.get(title)));
                            news.add(newsAnchor);
                        }
                    } catch (Exception e) {
                        Window.alert("Cannot deserialize news" + e.getLocalizedMessage());
                    }
                }

                @Override
                public void onMessage(@Nonnull WebSocket webSocket, @Nonnull ArrayBuffer arrayBuffer) {
                }

                @Override
                public void onError(@Nonnull WebSocket webSocket) {
                }
            });
            newsSocket.connect("ws://localhost:8080/news");
        }

        final WebSocket ratesSocket = WebSocket.newWebSocketIfSupported();
        if (ratesSocket != null) {
            ratesSocket.setListener(new WebSocketListener() {
                @Override
                public void onOpen(@Nonnull WebSocket webSocket) {
                }

                @Override
                public void onClose(@Nonnull WebSocket webSocket, boolean b, int i, @Nullable String s) {
                }

                @Override
                public void onMessage(@Nonnull WebSocket webSocket, @Nonnull String s) {
                    try {
                        JSONObject resultMapRates = new JSONObject(JsonUtils.safeEval(s));
                        currency.clear();
                        currency.add(new Label(CONSTANTS.sideMenuRates()));
                        String currencyInfo = "EUR " + getValueFromResult(resultMapRates.get(EUR_KEY)) +
                                " | USD " + getValueFromResult(resultMapRates.get(USD_KEY));
                        currency.add(new Label(currencyInfo));
                    } catch (Exception e) {
                        Window.alert("Cannot deserialize rates" + e.getLocalizedMessage());
                    }
                }

                @Override
                public void onMessage(@Nonnull WebSocket webSocket, @Nonnull ArrayBuffer arrayBuffer) {
                }

                @Override
                public void onError(@Nonnull WebSocket webSocket) {
                }
            });
            ratesSocket.connect("ws://localhost:8080/rates");
        }

        new JsonpRequestBuilder()
                .requestObject(NEWS_SERVLET, new AsyncCallback<JsArray<NewsJSO>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        LOGGER.log(Level.WARNING, "Something went wrong when tried to get news from RBC", caught);
                    }

                    @Override
                    public void onSuccess(JsArray<NewsJSO> result) {
                        news.clear();
                        for (int i = 0; i < result.length(); i++) {
                            NewsJSO newsJSO = result.get(i);
                            Anchor newsAnchor = new Anchor(newsJSO.getTitle());
                            newsAnchor.addStyleName("rbc-news-item-gwt");
                            newsAnchor.setHref(newsJSO.getUrl());
                            news.add(newsAnchor);
                        }
                    }
                });
    }

    private String getValueFromResult(JSONValue jsonValue) {
        String result = jsonValue.toString();
        return result.replaceAll("[\\[\\]\"]", "");
    }
}