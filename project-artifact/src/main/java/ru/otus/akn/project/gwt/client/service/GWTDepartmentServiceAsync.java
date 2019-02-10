package ru.otus.akn.project.gwt.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import ru.otus.akn.project.gwt.shared.Department;

import java.util.List;

public interface GWTDepartmentServiceAsync {
    void getAllDepartments(AsyncCallback<List<Department>> async);
}
