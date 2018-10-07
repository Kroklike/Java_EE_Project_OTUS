package ru.otus.akn.project.db;

import ru.otus.akn.project.db.entity.DepartmentEntity;
import ru.otus.akn.project.db.entity.EmployeeEntity;
import ru.otus.akn.project.db.entity.PositionEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static ru.otus.akn.project.db.dao.DepartmentsDAO.getAllDepartmentEntities;
import static ru.otus.akn.project.db.dao.EmployeesDAO.getAllEmployeeEntities;
import static ru.otus.akn.project.db.dao.PositionsDAO.getAllPositionEntities;

@WebServlet("/cleanTable")
public class TableCleanerServlet extends HttpServlet {

    private static final String PERSISTENCE_UNIT_NAME = "jpa";
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME); // for Tomcat

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        EntityManager manager = emf.createEntityManager();
        try {
            List<EmployeeEntity> entities = getAllEmployeeEntities(manager);
            List<DepartmentEntity> departmentEntities = getAllDepartmentEntities(manager);
            List<PositionEntity> positionEntities = getAllPositionEntities(manager);
            EntityTransaction transaction = manager.getTransaction();
            transaction.begin();
            for (EmployeeEntity entity : entities) {
                manager.remove(entity);
            }
            for (PositionEntity entity : positionEntities) {
                manager.remove(entity);
            }
            for (DepartmentEntity entity : departmentEntities) {
                manager.remove(entity);
            }
            transaction.commit();
            response.getWriter().println("All tables cleaned!");
        } catch (Exception e) {
            throw new ServletException(e);
        } finally {
            manager.close();
        }
    }
}
