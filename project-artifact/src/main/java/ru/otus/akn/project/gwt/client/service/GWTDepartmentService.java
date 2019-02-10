package ru.otus.akn.project.gwt.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import ru.otus.akn.project.gwt.shared.Department;

import java.util.List;

@RemoteServiceRelativePath("GWTDepartmentService")
public interface GWTDepartmentService extends RemoteService {
    List<Department> getAllDepartments();
}
