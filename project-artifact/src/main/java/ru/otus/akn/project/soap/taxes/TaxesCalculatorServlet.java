package ru.otus.akn.project.soap.taxes;

import com.google.gson.Gson;
import ru.otus.akn.project.soap.taxes.client.TaxesCalculatorProvider;
import ru.otus.akn.project.soap.taxes.client.TaxesCalculatorService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceRef;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

@WebServlet("/taxCalculator")
public class TaxesCalculatorServlet extends HttpServlet {

    @WebServiceRef
    private TaxesCalculatorService taxesCalculatorService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BigDecimal income = BigDecimal.valueOf(Double.valueOf(req.getParameter("income")));
        BigDecimal expenses = BigDecimal.valueOf(Double.valueOf(req.getParameter("expenses")));
        BigDecimal taxRate = BigDecimal.valueOf(Double.valueOf(req.getParameter("taxRate")));

        TaxesCalculatorProvider provider = taxesCalculatorService.getTaxesCalculatorProviderPort();
        BigDecimal taxes = provider.calculateTaxes(income, expenses, taxRate);

        String jsonResult = new Gson().toJson(taxes);
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            out.write(jsonResult);
        }
    }
}
