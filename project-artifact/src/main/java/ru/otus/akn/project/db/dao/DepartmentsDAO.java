package ru.otus.akn.project.db.dao;

import ru.otus.akn.project.db.entity.DepartmentEntity;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class DepartmentsDAO {

    public static DepartmentEntity getDepartmentEntity(EntityManager em, String departmentName) {
        Query deptQ = em.createQuery("select dept from DepartmentEntity dept where " +
                "dept.departmentName = :departmentName");
        deptQ.setParameter("departmentName", departmentName);
        return (DepartmentEntity) deptQ.getResultList().get(0);
    }

}
