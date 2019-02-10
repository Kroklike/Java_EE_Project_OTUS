package ru.otus.akn.project.soap.salary;

import ru.otus.akn.project.db.entity.EmployeeEntity;
import ru.otus.akn.project.ejb.api.stateless.EmployeesService;

import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebService;
import java.math.BigDecimal;
import java.util.List;

@WebService(serviceName = "SalaryCalculatorService", name = "SalaryCalculatorProvider")
public class SalaryCalculatorService implements SalaryCalculatorProvider {

    @EJB
    private EmployeesService employeesService;

    @WebMethod
    public BigDecimal getMaxEmployeesSalary() {
        List<EmployeeEntity> employeesWithMaxSalary = employeesService.getEmployeeEntitiesWithMaxSalary();
        return employeesWithMaxSalary.get(0).getSalary();
    }

    @WebMethod
    public BigDecimal calculateAverageSalary() {
        return employeesService.calculateAverageSalary();
    }
}
