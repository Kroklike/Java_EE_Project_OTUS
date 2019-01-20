package ru.otus.akn.project.client.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ru.otus.akn.project.client.servlets.RatesProcessor.getRates;
import static ru.otus.akn.project.util.OutputResultUtil.writeMapToResponse;

@WebServlet("/currencyRatesToJson")
public class CurrencyRatesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            writeMapToResponse(resp, getRates());
        } catch (Exception e) {
            throw new RuntimeException("Something goes wring when tried to get currency rates", e);
        }
    }
}
