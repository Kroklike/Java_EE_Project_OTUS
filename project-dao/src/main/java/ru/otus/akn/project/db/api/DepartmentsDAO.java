package ru.otus.akn.project.db.api;

import ru.otus.akn.project.db.entity.DepartmentEntity;

import javax.ejb.Local;
import javax.validation.constraints.NotNull;
import java.util.List;

@Local
public interface DepartmentsDAO {

    DepartmentEntity getDepartmentEntity(@NotNull String departmentName);

    DepartmentEntity getDepartmentEntity(@NotNull Long departmentId);

    List<DepartmentEntity> getAllDepartmentEntities();

    void deleteAllDepartmentsEntities();

    void deleteDepartmentEntityById(@NotNull Long departmentId);

    void saveAllDepartments(@NotNull List<DepartmentEntity> departmentEntities);

    DepartmentEntity saveDepartment(@NotNull DepartmentEntity departmentEntity);

    void updateAllDepartments(@NotNull List<DepartmentEntity> departmentEntities);

}
