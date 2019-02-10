package ru.otus.akn.project.ejb.api.stateless;

import ru.otus.akn.project.db.entity.DepartmentEntity;

import javax.ejb.Remote;
import javax.validation.constraints.NotNull;
import java.util.List;

@Remote
public interface DepartmentsService {

    DepartmentEntity getDepartmentEntity(@NotNull String departmentName);

    DepartmentEntity getDepartmentEntity(@NotNull Long departmentId);

    List<DepartmentEntity> getAllDepartmentEntities();

    void deleteAllDepartmentEntities();

    void deleteDepartmentEntityById(@NotNull Long departmentId);

    void saveAllDepartments(@NotNull List<DepartmentEntity> departmentEntities);

    DepartmentEntity saveDepartment(@NotNull DepartmentEntity departmentEntity);

    void updateAllDepartments(@NotNull List<DepartmentEntity> departmentEntities);

}
