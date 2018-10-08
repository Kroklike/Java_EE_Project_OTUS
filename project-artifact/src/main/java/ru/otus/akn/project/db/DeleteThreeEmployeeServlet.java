package ru.otus.akn.project.db;

import ru.otus.akn.project.db.entity.EmployeeEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.otus.akn.project.db.dao.EmployeesDAO.getAllEmployeeEntities;

@WebServlet("/deleteThreeEmployees")
public class DeleteThreeEmployeeServlet extends HttpServlet {

    private static final String PERSISTENCE_UNIT_NAME = "jpa";

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME); // for Tomcat

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        EntityManager manager = emf.createEntityManager();
        EntityTransaction transaction = manager.getTransaction();
        try {
            List<EmployeeEntity> entities = getAllEmployeeEntities(manager);
            Collections.shuffle(entities);
            List<EmployeeEntity> toDelete = entities.stream().limit(3).collect(Collectors.toList());
            if (toDelete.size() < 3) {
                throw new RuntimeException("Three employees did not find");
            }
            transaction.begin();
            for (EmployeeEntity entity : toDelete) {
                manager.remove(entity);
            }
            transaction.commit();
            response.getWriter().println("Three employees successfully deleted!");
        } catch (Exception e) {
            transaction.rollback();
            throw new ServletException(e);
        } finally {
            manager.close();
        }
    }
}
