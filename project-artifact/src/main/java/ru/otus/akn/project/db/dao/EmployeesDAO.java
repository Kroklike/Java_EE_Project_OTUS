package ru.otus.akn.project.db.dao;

import lombok.NonNull;
import org.hibernate.criterion.MatchMode;
import ru.otus.akn.project.db.entity.EmployeeEntity;
import ru.otus.akn.project.gwt.shared.Employee;
import ru.otus.akn.project.gwt.shared.Filter;
import ru.otus.akn.project.util.EntityManagerControl;
import ru.otus.akn.project.util.TransactionQueryConsumer;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.otus.akn.project.db.dao.DepartmentsDAO.getDepartmentEntity;
import static ru.otus.akn.project.db.dao.PositionsDAO.getPositionEntity;
import static ru.otus.akn.project.util.PersistenceUtil.MANAGER_FACTORY;

public class EmployeesDAO {

    public static List<EmployeeEntity> getAllEmployeeEntities(EntityManager em) {
        Query employeeQ = em.createQuery("select employee from EmployeeEntity employee ");
        return (List<EmployeeEntity>) employeeQ.getResultList();
    }

    public static List<EmployeeEntity> getEmployeeEntitiesByFilter(EntityManager em, Filter filter) {
        StringBuilder query = new StringBuilder("select employee from EmployeeEntity employee " +
                " inner join employee.positionEntity inner join employee.departmentEntity " +
                " where 1 = 1 ");

        boolean firstNameCheck = !filter.getFirstName().isEmpty();
        boolean lastNameCheck = !filter.getLastName().isEmpty();
        boolean middleNameCheck = !filter.getMiddleName().isEmpty();
        boolean positionCheck = !filter.getPosition().isEmpty();
        boolean townCheck = !filter.getTown().isEmpty();
        boolean ageCheckFrom = !filter.getAgeFrom().isEmpty();
        boolean ageCheckTo = !filter.getAgeTo().isEmpty();

        if (firstNameCheck) {
            query.append(" AND employee.firstName LIKE :firstName ");
        }
        if (lastNameCheck) {
            query.append(" AND employee.lastName LIKE :lastName ");
        }
        if (middleNameCheck) {
            query.append(" AND (employee.middleName IS NOT NULL AND employee.middleName LIKE :middleName) ");
        }
        if (positionCheck) {
            query.append(" AND employee.positionEntity.positionName LIKE :positionName");
        }
        if (townCheck) {
            query.append(" AND (employee.departmentEntity.city IS NOT NULL AND employee.departmentEntity.city LIKE :townName)");
        }

        Query employeeQ = em.createQuery(query.toString());
        if (firstNameCheck) {
            employeeQ.setParameter("firstName", MatchMode.ANYWHERE.toMatchString(filter.getFirstName()));
        }
        if (lastNameCheck) {
            employeeQ.setParameter("lastName", MatchMode.ANYWHERE.toMatchString(filter.getLastName()));
        }
        if (middleNameCheck) {
            employeeQ.setParameter("middleName", MatchMode.ANYWHERE.toMatchString(filter.getMiddleName()));
        }
        if (positionCheck) {
            employeeQ.setParameter("positionName", MatchMode.ANYWHERE.toMatchString(filter.getPosition()));
        }
        if (townCheck) {
            employeeQ.setParameter("townName", MatchMode.ANYWHERE.toMatchString(filter.getTown()));
        }

        List<EmployeeEntity> employeeEntities = (List<EmployeeEntity>) employeeQ.getResultList();

        LocalDate now = LocalDate.now();

        List<EmployeeEntity> result = new ArrayList<>();
        for (EmployeeEntity employeeEntity : employeeEntities) {
            Period period = Period.between(employeeEntity.getBirthdayDate(), now);
            int age = period.getYears();
            if ((ageCheckFrom && age < new BigDecimal(filter.getAgeFrom()).intValue())
                    || (ageCheckTo && age > new BigDecimal(filter.getAgeTo()).intValue())) {
                continue;
            }
            result.add(employeeEntity);
        }

        return result;
    }

    public static void deleteAllEmployeeEntities(EntityManager em) {
        new TransactionQueryConsumer(em) {
            @Override
            public void needToProcessData() {
                Query employeeQ = em.createQuery("delete from EmployeeEntity employee");
                employeeQ.executeUpdate();
            }
        }.processQueryInTransaction();
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

    public static void addNewEmployeeEntity(EntityManager em, @NonNull Employee employee) {
        new TransactionQueryConsumer(em) {
            @Override
            public void needToProcessData() {
                EmployeeEntity employeeEntity = new EmployeeEntity();
                employeeEntity.setFirstName(employee.getFirstName());
                employeeEntity.setLastName(employee.getLastName());
                if (employee.getMiddleName() != null && !employee.getMiddleName().isEmpty()) {
                    employeeEntity.setMiddleName(employee.getMiddleName());
                }
                employeeEntity.setSalary(employee.getSalary());
                employeeEntity.setEmploymentDate(LocalDate.now());
                employeeEntity.setBirthdayDate(LocalDate.now());
                employeeEntity.setBonusPercent(BigDecimal.ZERO);
                employeeEntity.setDepartmentEntity(getDepartmentEntity(em, employee.getDepartmentName()));
                employeeEntity.setPositionEntity(getPositionEntity(em, employee.getPositionName()));
                em.persist(employeeEntity);
            }
        }.processQueryInTransaction();
    }

    public static void saveAllEmployees(List<EmployeeEntity> employeeEntities, boolean fromContext) throws Exception {
        new EntityManagerControl(MANAGER_FACTORY) {
            @Override
            public void requestMethod(EntityManager manager) {
                new TransactionQueryConsumer(manager) {
                    @Override
                    public void needToProcessData() {
                        for (EmployeeEntity employeeEntity : employeeEntities) {
                            if (employeeEntity.getEmployeeId() != null) {
                                employeeEntity.setEmployeeId(null);
                            }
                            if (fromContext) {
                                employeeEntity.setPositionEntity(
                                        getPositionEntity(manager, employeeEntity.getPositionEntity().getPositionName()));
                                employeeEntity.setDepartmentEntity(
                                        getDepartmentEntity(manager, employeeEntity.getDepartmentEntity().getDepartmentName()));
                            }
                            manager.persist(employeeEntity);
                        }
                    }
                }.processQueryInTransaction();
            }
        }.processRequest();
    }

    public static void updateEmployeeEntity(EntityManager em, @NonNull Employee employee) {
        new TransactionQueryConsumer(em) {
            @Override
            public void needToProcessData() {
                Query employeeQ = em.createQuery("update EmployeeEntity set firstName = :firstName " +
                        ", lastName = :lastName, middleName = :middleName, salary = :salary " +
                        ", departmentEntity = :department, positionEntity = :position where id = :id");
                employeeQ.setParameter("firstName", employee.getFirstName());
                employeeQ.setParameter("lastName", employee.getLastName());
                employeeQ.setParameter("middleName", employee.getMiddleName().isEmpty() ?
                        null : employee.getMiddleName());
                employeeQ.setParameter("department", getDepartmentEntity(em, employee.getDepartmentName()));
                employeeQ.setParameter("position", getPositionEntity(em, employee.getPositionName()));
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
