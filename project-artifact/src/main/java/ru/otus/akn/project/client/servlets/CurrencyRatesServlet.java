package ru.otus.akn.project.client.servlets;

import org.json.JSONObject;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.otus.akn.project.util.Converter.convertStringWithXmlToDocument;

@WebServlet("/currencyRatesToJson")
public class CurrencyRatesServlet extends HttpServlet {

    private static final Map<String, BigDecimal> cachedRate = new HashMap<>();
    private static final String CBR_CURRENCY_RATES_URL = "http://www.cbr.ru/scripts/XML_daily.asp";
    private static final String EUR = "EUR";
    private static final String USD = "USD";
    private static final int TIMEOUT = 2000;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {

            if (!cachedRate.isEmpty()) {
                writeCurToResponse(resp);
                return;
            }

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

                List<String> curListToSearch = new ArrayList<>();
                curListToSearch.add(EUR);
                curListToSearch.add(USD);

                for (int i = 0; i < currency.getLength(); i++) {
                    Node node = currency.item(i);
                    findAndAddByCurrencyListName(node, curListToSearch);
                }
                writeCurToResponse(resp);
            }
        } catch (Exception e) {
            throw new RuntimeException("Something goes wring when try to get currency rates", e);
        }
    }

    private void writeCurToResponse(HttpServletResponse resp) throws IOException {
        JSONObject json = new JSONObject();
        for (Map.Entry<String, BigDecimal> entry : cachedRate.entrySet()) {
            json.put(entry.getKey(), entry.getValue());
        }
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            out.write(json.toString());
        }
    }

    private void findAndAddByCurrencyListName(Node node, List<String> curNames) {
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
            cachedRate.put(curName, curRate);
        }
    }
}
