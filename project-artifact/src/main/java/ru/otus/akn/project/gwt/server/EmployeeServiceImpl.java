package ru.otus.akn.project.gwt.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import ru.otus.akn.project.db.entity.EmployeeEntity;
import ru.otus.akn.project.gwt.client.service.EmployeeService;
import ru.otus.akn.project.gwt.shared.Employee;
import ru.otus.akn.project.util.EntityManagerControlGeneric;

import javax.persistence.EntityManager;
import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.List;

import static ru.otus.akn.project.db.dao.EmployeesDAO.getAllEmployeeEntities;
import static ru.otus.akn.project.util.PersistenceUtil.MANAGER_FACTORY;

@WebServlet("/Project/EmployeeService")
public class EmployeeServiceImpl extends RemoteServiceServlet implements EmployeeService {

    @Override
    public List<Employee> getAllEmployees() {

        List<EmployeeEntity> allEmployees;

        try {
            allEmployees = new EntityManagerControlGeneric<List<EmployeeEntity>>(MANAGER_FACTORY) {
                @Override
                public List<EmployeeEntity> requestMethod(EntityManager manager) {
                    return getAllEmployeeEntities(manager);
                }
            }.processRequest();
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong when tried to get employee entities from DB.");
        }

        List<Employee> result = new ArrayList<>();
        for (EmployeeEntity entity : allEmployees) {
            result.add(convertEmployeeEntityToEmployee(entity));
        }

        return result;
    }

    private Employee convertEmployeeEntityToEmployee(EmployeeEntity employeeEntity) {
        Employee employee = new Employee();
        employee.setFullName(employeeEntity.getFirstName() + " "
                + employeeEntity.getLastName() + " "
                + (employeeEntity.getMiddleName() == null ? "" : employeeEntity.getMiddleName()));
        employee.setDepartmentName(employeeEntity.getDepartmentEntity().getDepartmentName());
        employee.setPositionName(employeeEntity.getPositionEntity().getPositionName());
        employee.setSalary(employeeEntity.getSalary());
        return employee;
    }

}
