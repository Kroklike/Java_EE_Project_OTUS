package ru.otus.akn.project.ejb.services.stateless;

import ru.otus.akn.project.db.api.DepartmentsDAO;
import ru.otus.akn.project.db.entity.DepartmentEntity;
import ru.otus.akn.project.ejb.api.stateless.DepartmentsService;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.validation.constraints.NotNull;
import java.util.List;

@Stateless
public class DepartmentsServiceImpl implements DepartmentsService {

    @EJB
    private DepartmentsDAO departmentsDAO;

    @Override
    public DepartmentEntity getDepartmentEntity(@NotNull String departmentName) {
        return departmentsDAO.getDepartmentEntity(departmentName);
    }

    @Override
    public DepartmentEntity getDepartmentEntity(@NotNull Long departmentId) {
        return departmentsDAO.getDepartmentEntity(departmentId);
    }

    @Override
    public List<DepartmentEntity> getAllDepartmentEntities() {
        return departmentsDAO.getAllDepartmentEntities();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void deleteDepartmentEntityById(@NotNull Long departmentId) {
        departmentsDAO.deleteDepartmentEntityById(departmentId);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void updateAllDepartments(@NotNull List<DepartmentEntity> departmentEntities) {
        departmentsDAO.updateAllDepartments(departmentEntities);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void deleteAllDepartmentEntities() {
        departmentsDAO.deleteAllDepartmentsEntities();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void saveAllDepartments(@NotNull List<DepartmentEntity> departmentEntities) {
        departmentsDAO.saveAllDepartments(departmentEntities);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public DepartmentEntity saveDepartment(@NotNull DepartmentEntity departmentEntity) {
        return departmentsDAO.saveDepartment(departmentEntity);
    }
}
