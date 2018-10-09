package ru.otus.akn.project.db;

import ru.otus.akn.project.db.entity.DepartmentEntity;
import ru.otus.akn.project.db.entity.EmployeeEntity;
import ru.otus.akn.project.db.entity.PositionEntity;
import ru.otus.akn.project.db.util.EntityManagerControl;
import ru.otus.akn.project.db.util.TransactionQueryConsumer;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static ru.otus.akn.project.db.dao.DepartmentsDAO.getAllDepartmentEntities;
import static ru.otus.akn.project.db.dao.EmployeesDAO.getAllEmployeeEntities;
import static ru.otus.akn.project.db.dao.PositionsDAO.getAllPositionEntities;
import static ru.otus.akn.project.db.util.PersistenceUtil.MANAGER_FACTORY;

@WebServlet("/cleanTable")
public class TableCleanerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            new EntityManagerControl(MANAGER_FACTORY) {
                @Override
                public void requestMethod(EntityManager manager) {
                    List<EmployeeEntity> entities = getAllEmployeeEntities(manager);
                    List<DepartmentEntity> departmentEntities = getAllDepartmentEntities(manager);
                    List<PositionEntity> positionEntities = getAllPositionEntities(manager);
                    new TransactionQueryConsumer(manager) {
                        @Override
                        public void needToProcessData() {
                            for (EmployeeEntity entity : entities) {
                                manager.remove(entity);
                            }
                            for (PositionEntity entity : positionEntities) {
                                manager.remove(entity);
                            }
                            for (DepartmentEntity entity : departmentEntities) {
                                manager.remove(entity);
                            }
                        }
                    }.processQueryInTransaction();
                }
            }.processRequest();
            response.getWriter().println("All tables cleaned!");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
