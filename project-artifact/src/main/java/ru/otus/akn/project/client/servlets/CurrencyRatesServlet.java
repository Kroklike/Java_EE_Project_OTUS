package ru.otus.akn.project.client.servlets;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

import static ru.otus.akn.project.util.Converter.convertStringWithXmlToDocument;
import static ru.otus.akn.project.util.OutputResultUtil.parseInnerCurrency;
import static ru.otus.akn.project.util.OutputResultUtil.writeMapToResponse;

@WebServlet("/currencyRatesToJson")
public class CurrencyRatesServlet extends HttpServlet {

    private static final Map<String, BigDecimal> CACHED_RATE = new HashMap<>();
    private static final String CBR_CURRENCY_RATES_URL = "http://www.cbr.ru/scripts/XML_daily.asp";
    private static final List<String> CURRENCY_FOR_SEARCH = Arrays.asList("EUR", "USD");
    private static final int TIMEOUT = 2000;
    private static LocalDate lastUpdateDate;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {

            if (!CACHED_RATE.isEmpty()) {
                if (lastUpdateDate == null || lastUpdateDate.isBefore(LocalDate.now())) {
                    CACHED_RATE.clear();
                }
                else {
                    writeMapToResponse(resp, CACHED_RATE);
                    return;
                }
            }

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
                Document xmlDocument = convertStringWithXmlToDocument(result.toString());

                XPathFactory xPathFactory = XPathFactory.newInstance();
                XPath xPath = xPathFactory.newXPath();
                XPathExpression searchValute = xPath.compile("/ValCurs/Valute");

                NodeList currency = (NodeList) searchValute.evaluate(xmlDocument, XPathConstants.NODESET);

                for (int i = 0; i < currency.getLength(); i++) {
                    Node node = currency.item(i);
                    parseInnerCurrency(node, CURRENCY_FOR_SEARCH, CACHED_RATE);
                }
                lastUpdateDate = LocalDate.now();
                writeMapToResponse(resp, CACHED_RATE);
            }
        } catch (Exception e) {
            throw new RuntimeException("Something goes wring when tried to get currency rates", e);
        }
    }
}
