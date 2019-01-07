package ru.otus.akn.project.db.dao;

import ru.otus.akn.project.db.entity.DepartmentEntity;
import ru.otus.akn.project.util.EntityManagerControl;
import ru.otus.akn.project.util.TransactionQueryConsumer;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

import static ru.otus.akn.project.util.PersistenceUtil.MANAGER_FACTORY;

public class DepartmentsDAO {

    public static DepartmentEntity getDepartmentEntity(EntityManager em, String departmentName) {
        Query deptQ = em.createQuery("select dept from DepartmentEntity dept where " +
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

    public static void deleteAllDepartmentsEntities(EntityManager em) {
        new TransactionQueryConsumer(em) {
            @Override
            public void needToProcessData() {
                Query departmentsQ = em.createQuery("delete from DepartmentEntity department");
                departmentsQ.executeUpdate();
            }
        }.processQueryInTransaction();
    }

    public static void saveAllDepartments(List<DepartmentEntity> departmentEntities) throws Exception {
        new EntityManagerControl(MANAGER_FACTORY) {
            @Override
            public void requestMethod(EntityManager manager) {
                new TransactionQueryConsumer(manager) {
                    @Override
                    public void needToProcessData() {
                        for (DepartmentEntity departmentEntity : departmentEntities) {
                            if (departmentEntity.getDepartmentId() != null) {
                                departmentEntity.setDepartmentId(null);
                            }
                            manager.persist(departmentEntity);
                        }
                    }
                }.processQueryInTransaction();
            }
        }.processRequest();
    }

    public static List<DepartmentEntity> getAllDepartmentEntities(EntityManager em) {
        Query deptQ = em.createQuery("select dept from DepartmentEntity dept");
        return (List<DepartmentEntity>) deptQ.getResultList();
    }

}
