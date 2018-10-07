package ru.otus.akn.project.db.dao;

import ru.otus.akn.project.db.entity.DepartmentEntity;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

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

}
