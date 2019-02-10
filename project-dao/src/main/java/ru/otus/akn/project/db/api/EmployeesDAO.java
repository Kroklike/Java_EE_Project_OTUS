package ru.otus.akn.project.db.api;

import ru.otus.akn.project.db.entity.EmployeeEntity;
import ru.otus.akn.project.db.filters.EmployeeFilter;

import javax.ejb.Local;
import javax.validation.constraints.NotNull;
import java.util.List;

@Local
public interface EmployeesDAO {

    List<EmployeeEntity> getAllEmployeeEntities();

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
