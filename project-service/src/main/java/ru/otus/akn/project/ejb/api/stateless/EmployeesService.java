package ru.otus.akn.project.ejb.api.stateless;

import ru.otus.akn.project.db.entity.EmployeeEntity;
import ru.otus.akn.project.db.filters.EmployeeFilter;

import javax.ejb.Remote;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Remote
public interface EmployeesService {

    void changeLastNameAndPositionOfTwoEmployees();

    void deleteThreeEmployees();

    BigDecimal calculateAverageSalary();

    List<EmployeeEntity> getAllEmployeeEntities();

    List<EmployeeEntity> getAllEmployeeEntitiesOrderById();

    List<EmployeeEntity> getEmployeeEntitiesByFilter(@NotNull EmployeeFilter filter);

    List<EmployeeEntity> getEmployeeEntitiesWithMaxSalary();

    void deleteAllEmployeeEntities();

    void deleteEmployeeEntityById(@NotNull Long empId);

    void deleteEmployeeEntities(List<EmployeeEntity> employeeEntities);

    void addAllEmployees(@NotNull List<EmployeeEntity> employeeEntities);

    void addAllEmployees(@NotNull List<EmployeeEntity> employeeEntities, boolean fromContext);

    void updateEmployeeEntity(@NotNull EmployeeEntity employee);

    void updateEmployeeEntities(@NotNull List<EmployeeEntity> employees);

}
