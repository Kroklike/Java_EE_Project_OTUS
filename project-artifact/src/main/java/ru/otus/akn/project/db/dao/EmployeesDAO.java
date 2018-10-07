package ru.otus.akn.project.db.dao;

import ru.otus.akn.project.db.entity.EmployeeEntity;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class EmployeesDAO {

    public static List<EmployeeEntity> getAllEmployeeEntities(EntityManager em) {
        Query employeeQ = em.createQuery("select employee from EmployeeEntity employee ");
        return (List<EmployeeEntity>) employeeQ.getResultList();
    }

}
