package ru.otus.akn.project.ejb.services.singleton;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.otus.akn.project.ejb.api.singleton.RatesService;
import ru.otus.akn.project.util.Converter;

import javax.ejb.Singleton;
import javax.xml.xpath.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class RatesServiceImpl implements RatesService {

    private static final Map<String, BigDecimal> CACHED_RATE = new HashMap<>();
    private static final String CBR_CURRENCY_RATES_URL = "http://www.cbr.ru/scripts/XML_daily.asp";
    private static final List<String> CURRENCY_FOR_SEARCH = Arrays.asList("EUR", "USD");
    private static final int TIMEOUT = 2000;
    private static LocalDate lastUpdateDate;

    @Override
    public Map<String, BigDecimal> getRatesOrNull() throws IOException, XPathExpressionException {
        if (checkCacheRate()) return null;

        synchronized (CACHED_RATE) {

            if (checkCacheRate()) return null;

            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));

            URL url = new URL(CBR_CURRENCY_RATES_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            connection.setRequestProperty("Content-Type", "application/xml; charset=windows-1251");
            connection.setConnectTimeout(TIMEOUT);
            connection.setReadTimeout(TIMEOUT);

            int status = connection.getResponseCode();
            StringBuilder result = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "windows-1251"))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    result.append(new String(inputLine.getBytes(), StandardCharsets.UTF_8));
                }
            }

            if (status == 200 && result.length() > 0) {
                Document xmlDocument = Converter.convertStringWithXmlToDocument(result.toString());

                XPathFactory xPathFactory = XPathFactory.newInstance();
                XPath xPath = xPathFactory.newXPath();
                XPathExpression searchValute = xPath.compile("/ValCurs/Valute");

                NodeList currency = (NodeList) searchValute.evaluate(xmlDocument, XPathConstants.NODESET);

                for (int i = 0; i < currency.getLength(); i++) {
                    Node node = currency.item(i);
                    parseInnerCurrency(node, CURRENCY_FOR_SEARCH, CACHED_RATE);
                }
                lastUpdateDate = LocalDate.now();
                return CACHED_RATE;
            }

            return null;
        }
    }

    @Override
    public Map<String, BigDecimal> getRates() throws IOException, XPathExpressionException {
        Map<String, BigDecimal> ratesOrNull = getRatesOrNull();
        if (ratesOrNull == null) {
            return CACHED_RATE;
        } else {
            return ratesOrNull;
        }
    }

    private boolean checkCacheRate() {
        if (!CACHED_RATE.isEmpty()) {
            if (lastUpdateDate == null || lastUpdateDate.isBefore(LocalDate.now())) {
                CACHED_RATE.clear();
            } else {
                return true;
            }
        }
        return false;
    }

    private void parseInnerCurrency(Node node, List<String> curNames, Map<String, BigDecimal> result) {
        NodeList childNodes = node.getChildNodes();
        boolean needToAdd = false;
        String curName = null;
        BigDecimal curRate = null;
        for (int j = 0; j < childNodes.getLength(); j++) {
            Node childItem = childNodes.item(j);
            if (childItem.getNodeName().equals("CharCode")) {
                String charCodeValue = childItem.getFirstChild().getTextContent();
                if (curNames.contains(charCodeValue)) {
                    needToAdd = true;
                    curName = charCodeValue;
                }
            }
            if (childItem.getNodeName().equals("Value")) {
                String rateValue = childItem.getFirstChild().getTextContent()
                        .replace("\"", "")
                        .replace(",", ".");
                curRate = new BigDecimal(rateValue);
            }
        }
        if (needToAdd) {
            result.put(curName, curRate);
        }
    }
}
