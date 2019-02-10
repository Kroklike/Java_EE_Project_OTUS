package ru.otus.akn.project.db;

import ru.otus.akn.project.db.entity.EmployeeEntity;
import ru.otus.akn.project.ejb.api.stateless.EmployeesService;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/getEmployees")
public class GetEmployeeServlet extends HttpServlet {

    @EJB
    private EmployeesService employeesService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            List<EmployeeEntity> entitiesOrderById = employeesService.getAllEmployeeEntitiesOrderById();
            try (PrintWriter pw = response.getWriter()) {
                entitiesOrderById.forEach(pw::println);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
