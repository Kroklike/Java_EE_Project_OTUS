package ru.otus.akn.project.db;

import ru.otus.akn.project.db.entity.EmployeeEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

import static ru.otus.akn.project.db.dao.EmployeesDAO.getAllEmployeeEntitiesOrderById;

@WebServlet("/getEmployees")
public class GetEmployeeServlet extends HttpServlet {
    private static final String PERSISTENCE_UNIT_NAME = "jpa";

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME); // for Tomcat

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        EntityManager manager = emf.createEntityManager();
        try {
            List<EmployeeEntity> entitiesOrderById = getAllEmployeeEntitiesOrderById(manager);
            try (PrintWriter pw = response.getWriter()) {
                entitiesOrderById.forEach(pw::println);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        } finally {
            manager.close();
        }
    }
}
