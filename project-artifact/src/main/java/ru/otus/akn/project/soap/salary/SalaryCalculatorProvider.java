package ru.otus.akn.project.soap.salary;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import java.math.BigDecimal;

public interface SalaryCalculatorProvider {

    @WebMethod
    @WebResult(name = "maxSalary")
    BigDecimal getMaxEmployeesSalary();

    @WebMethod
    @WebResult(name = "avgSalary")
    BigDecimal calculateAverageSalary();

}
