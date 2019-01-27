package ru.otus.akn.project.soap.salary;

import ru.otus.akn.project.db.entity.EmployeeEntity;
import ru.otus.akn.project.util.EntityManagerControlGeneric;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static ru.otus.akn.project.db.dao.EmployeesDAO.getAllEmployeeEntities;
import static ru.otus.akn.project.util.PersistenceUtil.MANAGER_FACTORY;

@WebService(serviceName = "SalaryCalculatorService", name = "SalaryCalculatorProvider")
public class SalaryCalculatorService implements SalaryCalculatorProvider {

    @WebMethod
    public BigDecimal getMaxEmployeesSalary() {

        List<Object[]> employeeList;

        try {
            employeeList = new EntityManagerControlGeneric<List<Object[]>>(MANAGER_FACTORY) {
                @Override
                public List<Object[]> requestMethod(EntityManager manager) {
                    StoredProcedureQuery query = manager
                            .createStoredProcedureQuery("employee_with_max_salary")
                            .registerStoredProcedureParameter(1, Class.class,
                                    ParameterMode.REF_CURSOR);
                    query.execute();
                    return query.getResultList();
                }
            }.processRequest();

        } catch (Exception e) {
            throw new RuntimeException("Cannot get employees with max salary list from db", e);
        }

        if (employeeList != null && !employeeList.isEmpty()) {
            Object[] employee = employeeList.get(0);
            return (BigDecimal) employee[6];
        } else {
            throw new RuntimeException("Cannot found employees with max salary");
        }

    }

    @WebMethod
    public BigDecimal calculateAverageSalary() {

        List<EmployeeEntity> employees;

        try {
            employees = new EntityManagerControlGeneric<List<EmployeeEntity>>(MANAGER_FACTORY) {
                @Override
                public List<EmployeeEntity> requestMethod(EntityManager manager) {
                    return getAllEmployeeEntities(manager);
                }
            }.processRequest();
        } catch (Exception e) {
            throw new RuntimeException("Cannot get employees from db", e);
        }

        BigDecimal result = BigDecimal.ZERO;

        for (EmployeeEntity employee : employees) {

            if (employee.getSalary() == null || employee.getSalary().compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("Found employee with undefined or negative salary: " + employee.getSalary() +
                        ". EmployeeId: " + employee.getEmployeeId());
            }

            result = result.add(employee.getSalary());
        }

        return result.divide(BigDecimal.valueOf(employees.size()), 2, RoundingMode.HALF_UP);
    }
}
