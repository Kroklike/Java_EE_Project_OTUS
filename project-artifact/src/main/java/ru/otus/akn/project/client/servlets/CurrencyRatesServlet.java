package ru.otus.akn.project.client.servlets;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@WebServlet("/currencyRatesToJson")
public class CurrencyRatesServlet extends HttpServlet {

    private static final String CBR_CURRENCY_RATES_URL = "http://www.cbr.ru/scripts/XML_daily.asp";
    private static final int TIMEOUT = 2000;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
                result.append(new String(inputLine.getBytes(), StandardCharsets.UTF_8)).append("\n");
            }
        }

        try (ServletOutputStream out = resp.getOutputStream()) {
            out.println(result.toString());
        }
    }
}
