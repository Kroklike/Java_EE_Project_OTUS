package ru.otus.akn.project.db;

import ru.otus.akn.project.ejb.api.stateless.EmployeesService;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/deleteThreeEmployees")
public class DeleteThreeEmployeeServlet extends HttpServlet {

    @EJB
    private EmployeesService employeesService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            employeesService.deleteThreeEmployees();
            response.getWriter().println("Three employees successfully deleted!");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
