package ru.otus.akn.project.client.servlets;

import ru.otus.akn.project.ejb.api.singleton.RatesService;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ru.otus.akn.project.util.OutputResultUtil.writeMapToResponse;

@WebServlet("/currencyRatesToJson")
public class CurrencyRatesServlet extends HttpServlet {

    @EJB
    private RatesService ratesService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            writeMapToResponse(resp, ratesService.getRates());
        } catch (Exception e) {
            throw new RuntimeException("Something goes wring when tried to get currency rates", e);
        }
    }
}
