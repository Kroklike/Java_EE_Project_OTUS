package ru.otus.akn.project.db.dao;

import ru.otus.akn.project.db.entity.EmployeeEntity;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeesDAO {

    public static List<EmployeeEntity> getAllEmployeeEntities(EntityManager em) {
        Query employeeQ = em.createQuery("select employee from EmployeeEntity employee ");
        return (List<EmployeeEntity>) employeeQ.getResultList();
    }

    public static List<EmployeeEntity> getAllEmployeeEntitiesOrderById(EntityManager em) {
        List<EmployeeEntity> allEmployeeEntities = getAllEmployeeEntities(em);
        return allEmployeeEntities.stream()
                .sorted(Comparator.comparing(EmployeeEntity::getEmployeeId))
                .collect(Collectors.toList());
    }
}
