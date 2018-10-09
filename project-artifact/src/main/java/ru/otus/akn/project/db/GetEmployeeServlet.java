package ru.otus.akn.project.db;

import ru.otus.akn.project.db.entity.EmployeeEntity;
import ru.otus.akn.project.db.util.EntityManagerControlGeneric;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

import static ru.otus.akn.project.db.dao.EmployeesDAO.getAllEmployeeEntitiesOrderById;
import static ru.otus.akn.project.db.util.PersistenceUtil.MANAGER_FACTORY;

@WebServlet("/getEmployees")
public class GetEmployeeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            List<EmployeeEntity> entitiesOrderById = new EntityManagerControlGeneric<List<EmployeeEntity>>(MANAGER_FACTORY) {
                @Override
                public List<EmployeeEntity> requestMethod(EntityManager manager) {
                    return getAllEmployeeEntitiesOrderById(manager);
                }
            }.processRequest();
            try (PrintWriter pw = response.getWriter()) {
                entitiesOrderById.forEach(pw::println);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
