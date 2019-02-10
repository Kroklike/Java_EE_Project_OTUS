package ru.otus.akn.project.gwt.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import ru.otus.akn.project.db.entity.EmployeeEntity;
import ru.otus.akn.project.db.filters.EmployeeFilter;
import ru.otus.akn.project.ejb.api.stateless.DepartmentsService;
import ru.otus.akn.project.ejb.api.stateless.EmployeesService;
import ru.otus.akn.project.ejb.api.stateless.PositionsService;
import ru.otus.akn.project.gwt.client.service.GWTEmployeeService;
import ru.otus.akn.project.gwt.shared.Employee;
import ru.otus.akn.project.gwt.shared.Filter;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@WebServlet("/Project/GWTEmployeeService")
public class GWTEmployeeServiceImpl extends RemoteServiceServlet implements GWTEmployeeService {

    private static final Map<Filter, List<EmployeeEntity>> cache = new HashMap<>();

    @EJB
    private DepartmentsService departmentsService;
    @EJB
    private PositionsService positionsService;
    @EJB
    private EmployeesService employeesService;

    @Override
    public List<Employee> getAllEmployees() {

        List<EmployeeEntity> allEmployees = employeesService.getAllEmployeeEntities();
        List<Employee> result = new ArrayList<>();
        for (EmployeeEntity entity : allEmployees) {
            result.add(convertEmployeeEntityToEmployee(entity));
        }

        return result;
    }

    @Override
    public void addNewEmployee(Employee employee) {
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
        employeeEntity.setDepartmentEntity(departmentsService.getDepartmentEntity(employee.getDepartmentName()));
        employeeEntity.setPositionEntity(positionsService.getPositionEntity(employee.getPositionName()));
        employeesService.addAllEmployees(Collections.singletonList(employeeEntity));
    }

    @Override
    public void updateEmployee(Employee employee) {
        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setEmployeeId(employee.getId());
        employeeEntity.setFirstName(employee.getFirstName());
        employeeEntity.setLastName(employee.getLastName());
        if (employee.getMiddleName() != null && !employee.getMiddleName().isEmpty()) {
            employeeEntity.setMiddleName(employee.getMiddleName());
        }
        employeeEntity.setSalary(employee.getSalary());
        employeeEntity.setDepartmentEntity(departmentsService.getDepartmentEntity(employee.getDepartmentName()));
        employeeEntity.setPositionEntity(positionsService.getPositionEntity(employee.getPositionName()));
        employeesService.updateEmployeeEntity(employeeEntity);
    }

    @Override
    public void deleteEmployeeById(Long employeeId) {
        employeesService.deleteEmployeeEntityById(employeeId);
    }

    @Override
    public List<Employee> findEmployee(Filter filter) {
        List<EmployeeEntity> allEmployees;

        if (cache.get(filter) != null) {
            allEmployees = cache.get(filter);
        } else {
            EmployeeFilter employeeFilter = new EmployeeFilter();
            employeeFilter.setFirstName(filter.getFirstName());
            employeeFilter.setLastName(filter.getLastName());
            employeeFilter.setMiddleName(filter.getMiddleName());
            employeeFilter.setPosition(filter.getPosition());
            employeeFilter.setTown(filter.getTown());
            employeeFilter.setAgeFrom(filter.getAgeFrom());
            employeeFilter.setAgeTo(filter.getAgeTo());
            allEmployees = employeesService.getEmployeeEntitiesByFilter(employeeFilter);
            cache.put(filter, allEmployees);
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
