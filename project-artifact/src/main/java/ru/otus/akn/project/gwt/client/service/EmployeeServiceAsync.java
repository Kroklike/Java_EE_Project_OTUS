package ru.otus.akn.project.gwt.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import ru.otus.akn.project.gwt.shared.Employee;

import java.util.List;

public interface EmployeeServiceAsync {

    void getAllEmployees(AsyncCallback<List<Employee>> async);

    void deleteEmployeeById(Long employeeId, AsyncCallback<Void> async);

    void updateEmployee(Employee employee, AsyncCallback<Void> async);

    void addNewEmployee(Employee employee, AsyncCallback<Void> async);
}
