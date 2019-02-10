package ru.otus.akn.project.db;

import ru.otus.akn.project.ejb.api.stateless.TableService;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/cleanTable")
public class TableCleanerServlet extends HttpServlet {

    @EJB
    private TableService tableService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            tableService.cleanTables();
            response.getWriter().println("All tables cleaned!");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
