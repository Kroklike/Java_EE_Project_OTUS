package ru.otus.akn.project.db;

import ru.otus.akn.project.ejb.api.stateless.TableService;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@WebServlet("/fillTable")
public class TableFillerServlet extends HttpServlet {

    @EJB
    private TableService tableService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            ServletContext servletContext = this.getServletContext();
            PrintWriter writer = response.getWriter();
            writer.println(tableService.fillUpDepartmentsTable(servletContext));
            writer.println(tableService.fillUpPositionsTable(servletContext));
            writer.println(tableService.fillUpEmployeesTable(servletContext));
            writer.println(tableService.fillUpUsersTable(servletContext));
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
