package ru.otus.akn.project.gwt.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import ru.otus.akn.project.gwt.shared.Employee;

import java.util.List;

@RemoteServiceRelativePath("EmployeeService")
public interface EmployeeService extends RemoteService {

    List<Employee> getAllEmployees();

}