package ru.otus.akn.project.db.dao;

import lombok.NonNull;
import ru.otus.akn.project.db.entity.EmployeeEntity;
import ru.otus.akn.project.gwt.shared.Employee;
import ru.otus.akn.project.util.TransactionQueryConsumer;

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

    public static void deleteEmployeeEntityById(EntityManager em, @NonNull Long empId) {
        new TransactionQueryConsumer(em) {
            @Override
            public void needToProcessData() {
                Query employeeQ = em.createQuery("delete from EmployeeEntity employee where employee.id = :empId");
                employeeQ.setParameter("empId", empId);
                employeeQ.executeUpdate();
            }
        }.processQueryInTransaction();
    }

    public static void updateEmployeeEntity(EntityManager em, @NonNull Employee employee) {
        new TransactionQueryConsumer(em) {
            @Override
            public void needToProcessData() {
                String[] names = employee.getFullName().split(" ");
                String firstName = names[0];
                String lastName = names[1];
                String middleName;
                if (names.length == 2) {
                    middleName = "";
                } else {
                    middleName = names[2];
                }
                Query employeeQ = em.createQuery("update EmployeeEntity set firstName = :firstName " +
                        ", lastName = :lastName, middleName = :middleName, salary = :salary where id = :id");
                employeeQ.setParameter("firstName", firstName);
                employeeQ.setParameter("lastName", lastName);
                employeeQ.setParameter("middleName", middleName);
                employeeQ.setParameter("salary", employee.getSalary());
                employeeQ.setParameter("id", employee.getId());
                employeeQ.executeUpdate();
            }
        }.processQueryInTransaction();
    }

    public static List<EmployeeEntity> getAllEmployeeEntitiesOrderById(EntityManager em) {
        List<EmployeeEntity> allEmployeeEntities = getAllEmployeeEntities(em);
        return allEmployeeEntities.stream()
                .sorted(Comparator.comparing(EmployeeEntity::getEmployeeId))
                .collect(Collectors.toList());
    }
}
