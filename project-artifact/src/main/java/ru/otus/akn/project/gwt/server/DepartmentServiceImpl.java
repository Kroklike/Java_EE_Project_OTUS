package ru.otus.akn.project.gwt.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import ru.otus.akn.project.db.entity.DepartmentEntity;
import ru.otus.akn.project.gwt.client.service.DepartmentService;
import ru.otus.akn.project.gwt.shared.Department;
import ru.otus.akn.project.util.EntityManagerControlGeneric;

import javax.persistence.EntityManager;
import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.List;

import static ru.otus.akn.project.db.dao.DepartmentsDAO.getAllDepartmentEntities;
import static ru.otus.akn.project.util.PersistenceUtil.MANAGER_FACTORY;

@WebServlet("/Project/DepartmentService")
public class DepartmentServiceImpl extends RemoteServiceServlet implements DepartmentService {


    @Override
    public List<Department> getAllDepartments() {
        List<DepartmentEntity> allDepartments;

        try {
            allDepartments = new EntityManagerControlGeneric<List<DepartmentEntity>>(MANAGER_FACTORY) {
                @Override
                public List<DepartmentEntity> requestMethod(EntityManager manager) {
                    return getAllDepartmentEntities(manager);
                }
            }.processRequest();
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong when tried to get department entities from DB.", e);
        }

        List<Department> result = new ArrayList<>();
        for (DepartmentEntity entity : allDepartments) {
            result.add(new Department(entity.getDepartmentName()));
        }

        return result;
    }
}
