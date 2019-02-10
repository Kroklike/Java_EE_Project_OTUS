package ru.otus.akn.project.db.dao;

import lombok.NonNull;
import ru.otus.akn.project.db.api.DepartmentsDAO;
import ru.otus.akn.project.db.entity.DepartmentEntity;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.List;

@Stateless
public class DepartmentsDAOImpl implements DepartmentsDAO {

    @PersistenceContext(unitName = "jpa")
    private EntityManager entityManager;

    @Override
    public DepartmentEntity getDepartmentEntity(@NotNull String departmentName) {
        Query deptQ = entityManager.createQuery("select dept from DepartmentEntity dept where " +
                "dept.departmentName = :departmentName");
        deptQ.setParameter("departmentName", departmentName);
        List<DepartmentEntity> list = deptQ.getResultList();
        if (list.size() == 0) {
            throw new RuntimeException("Department not found");
        } else if (list.size() > 1) {
            throw new RuntimeException("Found more than one department");
        }
        return list.get(0);
    }

    @Override
    public DepartmentEntity getDepartmentEntity(@NotNull Long departmentId) {
        Query deptQ = entityManager.createQuery("select dept from DepartmentEntity dept where " +
                "dept.departmentId = :departmentId");
        deptQ.setParameter("departmentId", departmentId);
        List<DepartmentEntity> list = deptQ.getResultList();
        if (list.size() == 0) {
            throw new RuntimeException("Department not found");
        } else if (list.size() > 1) {
            throw new RuntimeException("Found more than one department");
        }
        return list.get(0);
    }

    @Override
    public List<DepartmentEntity> getAllDepartmentEntities() {
        Query deptQ = entityManager.createQuery("select dept from DepartmentEntity dept");
        return (List<DepartmentEntity>) deptQ.getResultList();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteAllDepartmentsEntities() {
        Query departmentsQ = entityManager.createQuery("delete from DepartmentEntity department");
        departmentsQ.executeUpdate();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteDepartmentEntityById(@NonNull Long departmentId) {
        Query departmentQ = entityManager.createQuery("delete from DepartmentEntity department " +
                "where department.departmentId = :departmentId");
        departmentQ.setParameter("departmentId", departmentId);
        departmentQ.executeUpdate();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveAllDepartments(@NotNull List<DepartmentEntity> departmentEntities) {
        for (DepartmentEntity departmentEntity : departmentEntities) {
            if (departmentEntity.getDepartmentId() != null) {
                departmentEntity.setDepartmentId(null);
            }
            entityManager.persist(departmentEntity);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public DepartmentEntity saveDepartment(@NotNull DepartmentEntity departmentEntity) {
        if (departmentEntity.getDepartmentId() != null) {
            departmentEntity.setDepartmentId(null);
        }
        entityManager.persist(departmentEntity);
        return departmentEntity;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void updateAllDepartments(@NotNull List<DepartmentEntity> departmentEntities) {
        for (DepartmentEntity departmentEntity : departmentEntities) {
            entityManager.merge(departmentEntity);
        }
    }
}
