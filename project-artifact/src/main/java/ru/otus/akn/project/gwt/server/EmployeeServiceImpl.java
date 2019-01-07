package ru.otus.akn.project.gwt.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import ru.otus.akn.project.db.entity.EmployeeEntity;
import ru.otus.akn.project.db.filters.EmployeeFilter;
import ru.otus.akn.project.gwt.client.service.EmployeeService;
import ru.otus.akn.project.gwt.shared.Employee;
import ru.otus.akn.project.gwt.shared.Filter;
import ru.otus.akn.project.util.EntityManagerControl;
import ru.otus.akn.project.util.EntityManagerControlGeneric;

import javax.persistence.EntityManager;
import javax.servlet.annotation.WebServlet;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static ru.otus.akn.project.db.dao.DepartmentsDAO.getDepartmentEntity;
import static ru.otus.akn.project.db.dao.EmployeesDAO.*;
import static ru.otus.akn.project.db.dao.PositionsDAO.getPositionEntity;
import static ru.otus.akn.project.util.PersistenceUtil.MANAGER_FACTORY;

@WebServlet("/Project/EmployeeService")
public class EmployeeServiceImpl extends RemoteServiceServlet implements EmployeeService {
    private static final Map<Filter, List<EmployeeEntity>> cache = new HashMap<>();

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
                    EmployeeEntity employeeEntity = new EmployeeEntity();
                    employeeEntity.setFirstName(employee.getFirstName());
                    employeeEntity.setLastName(employee.getLastName());
                    if (employee.getMiddleName() != null && !employee.getMiddleName().isEmpty()) {
                        employeeEntity.setMiddleName(employee.getMiddleName());
                    }
                    employeeEntity.setSalary(employee.getSalary());
                    employeeEntity.setEmploymentDate(LocalDate.now());
                    employeeEntity.setBirthdayDate(LocalDate.now());
                    employeeEntity.setBonusPercent(BigDecimal.ZERO);
                    employeeEntity.setDepartmentEntity(getDepartmentEntity(manager, employee.getDepartmentName()));
                    employeeEntity.setPositionEntity(getPositionEntity(manager, employee.getPositionName()));
                    saveAllEmployees(manager, Collections.singletonList(employeeEntity));
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
                    EmployeeEntity employeeEntity = new EmployeeEntity();
                    employeeEntity.setEmployeeId(employee.getId());
                    employeeEntity.setFirstName(employee.getFirstName());
                    employeeEntity.setLastName(employee.getLastName());
                    if (employee.getMiddleName() != null && !employee.getMiddleName().isEmpty()) {
                        employeeEntity.setMiddleName(employee.getMiddleName());
                    }
                    employeeEntity.setSalary(employee.getSalary());
                    employeeEntity.setDepartmentEntity(getDepartmentEntity(manager, employee.getDepartmentName()));
                    employeeEntity.setPositionEntity(getPositionEntity(manager, employee.getPositionName()));
                    updateEmployeeEntity(manager, employeeEntity);
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

    @Override
    public List<Employee> findEmployee(Filter filter) {
        List<EmployeeEntity> allEmployees;

        if (cache.get(filter) != null) {
            allEmployees = cache.get(filter);
        } else {
            try {
                allEmployees = new EntityManagerControlGeneric<List<EmployeeEntity>>(MANAGER_FACTORY) {
                    @Override
                    public List<EmployeeEntity> requestMethod(EntityManager manager) {
                        EmployeeFilter employeeFilter = new EmployeeFilter();
                        employeeFilter.setFirstName(filter.getFirstName());
                        employeeFilter.setLastName(filter.getLastName());
                        employeeFilter.setMiddleName(filter.getMiddleName());
                        employeeFilter.setPosition(filter.getPosition());
                        employeeFilter.setTown(filter.getTown());
                        employeeFilter.setAgeFrom(filter.getAgeFrom());
                        employeeFilter.setAgeTo(filter.getAgeTo());
                        return getEmployeeEntitiesByFilter(manager, employeeFilter);
                    }
                }.processRequest();
                cache.put(filter, allEmployees);
            } catch (Exception e) {
                throw new RuntimeException("Something went wrong when tried to get employee entities from DB by filter.", e);
            }
        }

        List<Employee> result = new ArrayList<>();
        for (EmployeeEntity entity : allEmployees) {
            result.add(convertEmployeeEntityToEmployee(entity));
        }

        this.getThreadLocalRequest().getSession().setAttribute("empList", allEmployees);

        return result;
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
