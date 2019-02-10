package ru.otus.akn.project.soap.salary;

import com.google.gson.Gson;
import ru.otus.akn.project.soap.salary.client.SalaryCalculatorProvider;
import ru.otus.akn.project.soap.salary.client.SalaryCalculatorService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceRef;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/salaryCalculatorAvg")
public class SalaryCalculatorAvgServlet extends HttpServlet {

    @WebServiceRef
    private SalaryCalculatorService calculatorService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        SalaryCalculatorProvider provider = calculatorService.getSalaryCalculatorProviderPort();

        String jsonResult = new Gson().toJson(provider.calculateAverageSalary());
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            out.write(jsonResult);
        }
    }
}
