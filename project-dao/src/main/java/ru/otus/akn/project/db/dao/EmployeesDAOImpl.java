package ru.otus.akn.project.db.dao;

import org.hibernate.criterion.MatchMode;
import ru.otus.akn.project.db.api.DepartmentsDAO;
import ru.otus.akn.project.db.api.EmployeesDAO;
import ru.otus.akn.project.db.api.PositionsDAO;
import ru.otus.akn.project.db.api.UsersDAO;
import ru.otus.akn.project.db.entity.EmployeeEntity;
import ru.otus.akn.project.db.filters.EmployeeFilter;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class EmployeesDAOImpl implements EmployeesDAO {

    private static final String PROCEDURE_MAX_SALARY = "employee_with_max_salary";

    @PersistenceContext(unitName = "jpa")
    private EntityManager entityManager;

    @EJB
    private DepartmentsDAO departmentsDAO;
    @EJB
    private PositionsDAO positionsDAO;
    @EJB
    private UsersDAO usersDAO;

    @Override
    public List<EmployeeEntity> getAllEmployeeEntities() {
        Query employeeQ = entityManager.createQuery("select employee from EmployeeEntity employee ");
        return (List<EmployeeEntity>) employeeQ.getResultList();
    }

    @Override
    public List<EmployeeEntity> getEmployeeEntitiesByFilter(@NotNull EmployeeFilter filter) {
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

        Query employeeQ = entityManager.createQuery(query.toString());
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

    @Override
    public List<EmployeeEntity> getEmployeeEntitiesWithMaxSalary() {
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery(PROCEDURE_MAX_SALARY)
                .registerStoredProcedureParameter(1, Class.class, ParameterMode.REF_CURSOR);
        query.execute();

        List<Object[]> employeeList = query.getResultList();
        List<EmployeeEntity> result = new ArrayList<>();

        if (employeeList != null && !employeeList.isEmpty()) {
            for (Object[] employee : employeeList) {
                EmployeeEntity employeeEntity = new EmployeeEntity();
                employeeEntity.setEmployeeId((Long) employee[0]);
                employeeEntity.setPositionEntity(positionsDAO.getPositionEntity((Long) employee[1]));
                employeeEntity.setDepartmentEntity(departmentsDAO.getDepartmentEntity((Long) employee[2]));
                employeeEntity.setFirstName((String) employee[3]);
                employeeEntity.setLastName((String) employee[4]);
                employeeEntity.setEmploymentDate((LocalDate) employee[5]);
                employeeEntity.setSalary((BigDecimal) employee[6]);
                employeeEntity.setBonusPercent((BigDecimal) employee[7]);
                employeeEntity.setMiddleName(employee[8] == null ? null : (String) employee[8]);
                employeeEntity.setTelephoneNumber(employee[9] == null ? null : (String) employee[9]);
                employeeEntity.setEmail(employee[10] == null ? null : (String) employee[10]);
                employeeEntity.setDismissalDate(employee[11] == null ? null : (LocalDate) employee[11]);
                employeeEntity.setUserEntity(employee[12] == null ? null :
                        usersDAO.getUserEntityById((Long) employee[12]));
                employeeEntity.setBirthdayDate((LocalDate) employee[13]);
                result.add(employeeEntity);
            }
        } else {
            throw new RuntimeException("Cannot found employees with max salary");
        }
        return result;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteAllEmployeeEntities() {
        Query employeeQ = entityManager.createQuery("delete from EmployeeEntity employee");
        employeeQ.executeUpdate();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteEmployeeEntityById(@NotNull Long empId) {
        Query employeeQ = entityManager.createQuery("delete from EmployeeEntity employee where employee.id = :empId");
        employeeQ.setParameter("empId", empId);
        employeeQ.executeUpdate();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteEmployeeEntities(List<EmployeeEntity> employeeEntities) {
        for (EmployeeEntity employeeEntity : employeeEntities) {
            entityManager.remove(employeeEntity);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void addAllEmployees(@NotNull List<EmployeeEntity> employeeEntities) {
        for (EmployeeEntity employeeEntity : employeeEntities) {
            if (employeeEntity.getEmployeeId() != null) {
                employeeEntity.setEmployeeId(null);
            }
            entityManager.persist(employeeEntity);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void addAllEmployees(@NotNull List<EmployeeEntity> employeeEntities, boolean fromContext) {
        for (EmployeeEntity employeeEntity : employeeEntities) {
            if (employeeEntity.getEmployeeId() != null) {
                employeeEntity.setEmployeeId(null);
            }
            if (fromContext) {
                employeeEntity.setPositionEntity(
                        positionsDAO.getPositionEntity(employeeEntity.getPositionEntity().getPositionName()));
                employeeEntity.setDepartmentEntity(
                        departmentsDAO.getDepartmentEntity(employeeEntity.getDepartmentEntity().getDepartmentName()));
            }
            entityManager.persist(employeeEntity);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void updateEmployeeEntity(@NotNull EmployeeEntity employee) {
        Query employeeQ = entityManager.createQuery("update EmployeeEntity set firstName = :firstName " +
                ", lastName = :lastName, middleName = :middleName, salary = :salary " +
                ", departmentEntity = :department, positionEntity = :position where id = :id");
        employeeQ.setParameter("firstName", employee.getFirstName());
        employeeQ.setParameter("lastName", employee.getLastName());
        employeeQ.setParameter("middleName", employee.getMiddleName().isEmpty() ?
                null : employee.getMiddleName());
        employeeQ.setParameter("department", employee.getDepartmentEntity());
        employeeQ.setParameter("position", employee.getPositionEntity());
        employeeQ.setParameter("salary", employee.getSalary());
        employeeQ.setParameter("id", employee.getEmployeeId());
        employeeQ.executeUpdate();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void updateEmployeeEntities(List<EmployeeEntity> employees) {
        for (EmployeeEntity employee : employees) {
            entityManager.persist(employee);
        }
    }
}
