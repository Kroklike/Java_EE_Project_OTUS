package ru.otus.akn.project.db;

import ru.otus.akn.project.db.entity.EmployeeEntity;
import ru.otus.akn.project.db.entity.PositionEntity;
import ru.otus.akn.project.db.util.EntityManagerControl;
import ru.otus.akn.project.db.util.Generator;
import ru.otus.akn.project.db.util.TransactionQueryConsumer;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.otus.akn.project.db.dao.EmployeesDAO.getAllEmployeeEntitiesOrderById;
import static ru.otus.akn.project.db.dao.PositionsDAO.getAllPositionEntities;
import static ru.otus.akn.project.db.util.PersistenceUtil.MANAGER_FACTORY;

@WebServlet("/changeLastNameAndPosition")
public class ChangeEmployeeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            new EntityManagerControl(MANAGER_FACTORY) {
                @Override
                public void requestMethod(EntityManager manager) {
                    List<EmployeeEntity> entitiesOrderById = getAllEmployeeEntitiesOrderById(manager);
                    Collections.shuffle(entitiesOrderById);
                    List<PositionEntity> positions = getAllPositionEntities(manager);
                    Collections.shuffle(positions);
                    List<EmployeeEntity> entities = entitiesOrderById.stream().limit(2).collect(Collectors.toList());
                    Optional<PositionEntity> positionEntity = positions.stream().limit(1).findFirst();
                    if (entities.size() < 2) {
                        throw new RuntimeException("Two employees did not find");
                    } else if (!positionEntity.isPresent()) {
                        throw new RuntimeException("Position did not find");
                    } else {
                        new TransactionQueryConsumer(manager) {
                            @Override
                            public void needToProcessData() {
                                for (EmployeeEntity entity : entities) {
                                    entity.setLastName(Generator.generateName());
                                    entity.setPositionEntity(positionEntity.get());
                                    manager.persist(entity);
                                }
                            }
                        }.processQueryInTransaction();
                    }
                }
            }.processRequest();
            response.getWriter().println("Two employees successfully changed!");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

}
