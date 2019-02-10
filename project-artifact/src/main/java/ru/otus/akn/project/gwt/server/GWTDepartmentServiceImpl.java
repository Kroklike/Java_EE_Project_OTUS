package ru.otus.akn.project.gwt.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import ru.otus.akn.project.db.entity.DepartmentEntity;
import ru.otus.akn.project.ejb.api.stateless.DepartmentsService;
import ru.otus.akn.project.gwt.client.service.GWTDepartmentService;
import ru.otus.akn.project.gwt.shared.Department;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/Project/GWTDepartmentService")
public class GWTDepartmentServiceImpl extends RemoteServiceServlet implements GWTDepartmentService {

    @EJB
    private DepartmentsService departmentsService;

    @Override
    public List<Department> getAllDepartments() {
        List<DepartmentEntity> allDepartments = departmentsService.getAllDepartmentEntities();

        List<Department> result = new ArrayList<>();
        for (DepartmentEntity entity : allDepartments) {
            result.add(new Department(entity.getDepartmentName()));
        }

        return result;
    }
}
