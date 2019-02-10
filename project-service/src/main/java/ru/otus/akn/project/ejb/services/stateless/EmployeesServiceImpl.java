package ru.otus.akn.project.ejb.services.stateless;

import ru.otus.akn.project.db.api.EmployeesDAO;
import ru.otus.akn.project.db.entity.EmployeeEntity;
import ru.otus.akn.project.db.entity.PositionEntity;
import ru.otus.akn.project.db.filters.EmployeeFilter;
import ru.otus.akn.project.ejb.api.stateless.EmployeesService;
import ru.otus.akn.project.ejb.api.stateless.PositionsService;
import ru.otus.akn.project.util.Generator;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
public class EmployeesServiceImpl implements EmployeesService {

    @EJB
    private PositionsService positionsService;
    @EJB
    private EmployeesDAO employeesDAO;

    @Override
    public List<EmployeeEntity> getAllEmployeeEntities() {
        return employeesDAO.getAllEmployeeEntities();
    }

    @Override
    public List<EmployeeEntity> getEmployeeEntitiesByFilter(EmployeeFilter filter) {
        return employeesDAO.getEmployeeEntitiesByFilter(filter);
    }

    @Override
    public List<EmployeeEntity> getEmployeeEntitiesWithMaxSalary() {
        return employeesDAO.getEmployeeEntitiesWithMaxSalary();
    }

    @Override
    public List<EmployeeEntity> getAllEmployeeEntitiesOrderById() {
        List<EmployeeEntity> allEmployeeEntities = getAllEmployeeEntities();
        return allEmployeeEntities.stream()
                .sorted(Comparator.comparing(EmployeeEntity::getEmployeeId))
                .collect(Collectors.toList());
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void changeLastNameAndPositionOfTwoEmployees() {
        List<EmployeeEntity> entitiesOrderById = getAllEmployeeEntitiesOrderById();
        Collections.shuffle(entitiesOrderById);
        List<PositionEntity> positions = positionsService.getAllPositionEntities();
        Collections.shuffle(positions);
        List<EmployeeEntity> entities = entitiesOrderById.stream().limit(2).collect(Collectors.toList());
        Optional<PositionEntity> positionEntity = positions.stream().limit(1).findFirst();
        if (entities.size() < 2) {
            throw new RuntimeException("Two employees did not find");
        } else if (!positionEntity.isPresent()) {
            throw new RuntimeException("Position did not find");
        } else {
            for (EmployeeEntity entity : entities) {
                entity.setLastName(Generator.generateName());
                entity.setPositionEntity(positionEntity.get());
            }
            updateEmployeeEntities(entities);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void deleteThreeEmployees() {
        List<EmployeeEntity> entities = getAllEmployeeEntities();
        Collections.shuffle(entities);
        List<EmployeeEntity> toDelete = entities.stream().limit(3).collect(Collectors.toList());
        if (toDelete.size() < 3) {
            throw new RuntimeException("Three employees did not find");
        }
        deleteEmployeeEntities(toDelete);
    }

    @Override
    public BigDecimal calculateAverageSalary() {
        List<EmployeeEntity> employees = getAllEmployeeEntities();
        BigDecimal result = BigDecimal.ZERO;

        for (EmployeeEntity employee : employees) {

            if (employee.getSalary() == null || employee.getSalary().compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("Found employee with undefined or negative salary: " + employee.getSalary() +
                        ". EmployeeId: " + employee.getEmployeeId());
            }

            result = result.add(employee.getSalary());
        }

        return result.divide(BigDecimal.valueOf(employees.size()), 2, RoundingMode.HALF_UP);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void deleteAllEmployeeEntities() {
        employeesDAO.deleteAllEmployeeEntities();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void deleteEmployeeEntityById(Long empId) {
        employeesDAO.deleteEmployeeEntityById(empId);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void deleteEmployeeEntities(List<EmployeeEntity> employeeEntities) {
        employeesDAO.deleteEmployeeEntities(employeeEntities);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void addAllEmployees(List<EmployeeEntity> employeeEntities) {
        employeesDAO.addAllEmployees(employeeEntities);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void addAllEmployees(List<EmployeeEntity> employeeEntities, boolean fromContext) {
        employeesDAO.addAllEmployees(employeeEntities, fromContext);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void updateEmployeeEntity(EmployeeEntity employee) {
        employeesDAO.updateEmployeeEntity(employee);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void updateEmployeeEntities(List<EmployeeEntity> employees) {
        employeesDAO.updateEmployeeEntities(employees);
    }
}
