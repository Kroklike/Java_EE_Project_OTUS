package ru.otus.akn.project.gwt.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import ru.otus.akn.project.db.entity.EmployeeEntity;
import ru.otus.akn.project.gwt.client.service.EmployeeService;
import ru.otus.akn.project.gwt.shared.Employee;
import ru.otus.akn.project.util.EntityManagerControl;
import ru.otus.akn.project.util.EntityManagerControlGeneric;

import javax.persistence.EntityManager;
import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.List;

import static ru.otus.akn.project.db.dao.EmployeesDAO.*;
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
            throw new RuntimeException("Something went wrong when tried to get employee entities from DB.", e);
        }

        List<Employee> result = new ArrayList<>();
        for (EmployeeEntity entity : allEmployees) {
            result.add(convertEmployeeEntityToEmployee(entity));
        }

        return result;
    }

    @Override
    public void addNewEmployee(Employee employee) {
        try {
            new EntityManagerControl(MANAGER_FACTORY) {
                @Override
                public void requestMethod(EntityManager manager) {
                    addNewEmployeeEntity(manager, employee);
                }
            }.processRequest();
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong when tried to add employee entity.", e);
        }
    }

    @Override
    public void updateEmployee(Employee employee) {
        try {
            new EntityManagerControl(MANAGER_FACTORY) {
                @Override
                public void requestMethod(EntityManager manager) {
                    updateEmployeeEntity(manager, employee);
                }
            }.processRequest();
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong when tried to update employee entity by id.", e);
        }
    }

    @Override
    public void deleteEmployeeById(Long employeeId) {
        try {
            new EntityManagerControl(MANAGER_FACTORY) {
                @Override
                public void requestMethod(EntityManager manager) {
                    deleteEmployeeEntityById(manager, employeeId);
                }
            }.processRequest();
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong when tried to delete employee entity from DB by id.", e);
        }
    }

    private Employee convertEmployeeEntityToEmployee(EmployeeEntity employeeEntity) {
        Employee employee = new Employee();
        employee.setId(employeeEntity.getEmployeeId());
        employee.setFirstName(employeeEntity.getFirstName());
        employee.setLastName(employeeEntity.getLastName());
        employee.setMiddleName(employeeEntity.getMiddleName() == null ? "" : employeeEntity.getMiddleName());
        employee.setDepartmentName(employeeEntity.getDepartmentEntity().getDepartmentName());
        employee.setPositionName(employeeEntity.getPositionEntity().getPositionName());
        employee.setSalary(employeeEntity.getSalary());
        return employee;
    }

}
