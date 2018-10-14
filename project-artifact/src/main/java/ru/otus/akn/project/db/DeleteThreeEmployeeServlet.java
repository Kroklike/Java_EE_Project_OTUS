package ru.otus.akn.project.db;

import ru.otus.akn.project.db.entity.EmployeeEntity;
import ru.otus.akn.project.util.EntityManagerControl;
import ru.otus.akn.project.util.TransactionQueryConsumer;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.otus.akn.project.db.dao.EmployeesDAO.getAllEmployeeEntities;
import static ru.otus.akn.project.util.PersistenceUtil.MANAGER_FACTORY;

@WebServlet("/deleteThreeEmployees")
public class DeleteThreeEmployeeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            new EntityManagerControl(MANAGER_FACTORY) {
                @Override
                public void requestMethod(EntityManager manager) {
                    List<EmployeeEntity> entities = getAllEmployeeEntities(manager);
                    Collections.shuffle(entities);
                    List<EmployeeEntity> toDelete = entities.stream().limit(3).collect(Collectors.toList());
                    if (toDelete.size() < 3) {
                        throw new RuntimeException("Three employees did not find");
                    }
                    new TransactionQueryConsumer(manager) {
                        @Override
                        public void needToProcessData() {
                            for (EmployeeEntity entity : toDelete) {
                                manager.remove(entity);
                            }
                        }
                    }.processQueryInTransaction();
                }
            }.processRequest();
            response.getWriter().println("Three employees successfully deleted!");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
