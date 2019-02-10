package ru.otus.akn.project.ejb.services.stateless;

import com.opencsv.CSVReader;
import org.apache.commons.codec.digest.DigestUtils;
import ru.otus.akn.project.db.entity.DepartmentEntity;
import ru.otus.akn.project.db.entity.EmployeeEntity;
import ru.otus.akn.project.db.entity.PositionEntity;
import ru.otus.akn.project.db.entity.UserEntity;
import ru.otus.akn.project.ejb.api.stateless.*;
import ru.otus.akn.project.util.ResourceUtil;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.servlet.ServletContext;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class TableServiceImpl implements TableService {

    private static final String CSV_DEPARTMENTS = "/WEB-INF/classes/DATA/DEPARTMENTS.csv";
    private static final String CSV_POSITIONS = "/WEB-INF/classes/DATA/POSITIONS.csv";
    private static final String CSV_EMPLOYEES = "/WEB-INF/classes/DATA/EMPLOYEES.csv";
    private static final String CSV_USERS = "/WEB-INF/classes/DATA/USERS.csv";
    private static final String DATE_FORMAT = "dd.MM.yyyy";
    private static final char CSV_SPLITTER = ';';
    private static final String DEPARTMENTS_FILLED_MESSAGE = "Departments table have already filled";
    private static final String USERS_FILLED_MESSAGE = "Users table have already filled";
    private static final String POSITIONS_FILLED_MESSAGE = "Positions table have already filled";
    private static final String EMPLOYEES_FILLED_MESSAGE = "Employees table have already filled";
    private static final String DEPARTMENTS_SUCCESS_MESSAGE = "Departments table filled";
    private static final String USERS_SUCCESS_MESSAGE = "Users table filled";
    private static final String POSITIONS_SUCCESS_MESSAGE = "Positions table filled";
    private static final String EMPLOYEES_SUCCESS_MESSAGE = "Employees table filled";

    @EJB
    private EmployeesService employeesService;
    @EJB
    private DepartmentsService departmentsService;
    @EJB
    private PositionsService positionsService;
    @EJB
    private UsersService usersService;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void cleanTables() {
        employeesService.deleteAllEmployeeEntities();
        departmentsService.deleteAllDepartmentEntities();
        positionsService.deleteAllPositionsEntities();
        usersService.deleteAllUsersEntities();
    }

    @Override
    public String fillUpDepartmentsTable(@NotNull ServletContext context) throws IOException {
        if (departmentsService.getAllDepartmentEntities().size() != 0) {
            return DEPARTMENTS_FILLED_MESSAGE;
        }
        List<DepartmentEntity> toSave = new ArrayList<>();
        try (CSVReader reader = new CSVReader(ResourceUtil.getFileAsBufferedReader(context, CSV_DEPARTMENTS), CSV_SPLITTER)) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                DepartmentEntity departmentEntity = new DepartmentEntity();
                departmentEntity.setDepartmentName(nextLine[0]);
                departmentEntity.setCity(nextLine[1]);
                departmentEntity.setFullAddress(nextLine[2]);
                toSave.add(departmentEntity);
            }
        }
        if (!toSave.isEmpty()) {
            departmentsService.saveAllDepartments(toSave);
        }
        return DEPARTMENTS_SUCCESS_MESSAGE;
    }

    @Override
    public String fillUpUsersTable(@NotNull ServletContext context) throws IOException {
        if (usersService.getAllUsersEntities().size() != 0) {
            return USERS_FILLED_MESSAGE;
        }
        List<UserEntity> toSave = new ArrayList<>();
        try (CSVReader reader = new CSVReader(ResourceUtil.getFileAsBufferedReader(context, CSV_USERS), CSV_SPLITTER)) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                UserEntity userEntity = new UserEntity();
                userEntity.setLogin(nextLine[0]);
                userEntity.setPasswordHash(DigestUtils.sha256Hex(nextLine[1]));
                userEntity.setIsActive(Boolean.parseBoolean(nextLine[2]));
                toSave.add(userEntity);
            }
        }
        if (!toSave.isEmpty()) {
            usersService.saveAllUsers(toSave);
        }
        return USERS_SUCCESS_MESSAGE;
    }

    @Override
    public String fillUpPositionsTable(@NotNull ServletContext context) throws IOException {
        if (positionsService.getAllPositionEntities().size() != 0) {
            return POSITIONS_FILLED_MESSAGE;
        }
        List<PositionEntity> toSave = new ArrayList<>();
        try (CSVReader reader = new CSVReader(ResourceUtil.getFileAsBufferedReader(context, CSV_POSITIONS), CSV_SPLITTER)) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                PositionEntity positionEntity = new PositionEntity();
                positionEntity.setPositionName(nextLine[0]);
                toSave.add(positionEntity);
            }
        }
        if (!toSave.isEmpty()) {
            positionsService.saveAllPositions(toSave);
        }
        return POSITIONS_SUCCESS_MESSAGE;
    }

    @Override
    public String fillUpEmployeesTable(ServletContext context) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        if (employeesService.getAllEmployeeEntities().size() != 0) {
            return EMPLOYEES_FILLED_MESSAGE;
        }
        try (CSVReader reader = new CSVReader(ResourceUtil.getFileAsBufferedReader(context, CSV_EMPLOYEES), CSV_SPLITTER)) {
            String[] nextLine;
            List<EmployeeEntity> toSave = new ArrayList<>();
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length == 0) {
                    continue;
                }
                EmployeeEntity employeeEntity = new EmployeeEntity();
                employeeEntity.setPositionEntity(positionsService.getPositionEntity(nextLine[0]));
                employeeEntity.setDepartmentEntity(departmentsService.getDepartmentEntity(nextLine[1]));
                employeeEntity.setFirstName(nextLine[2]);
                employeeEntity.setLastName(nextLine[3]);
                employeeEntity.setEmploymentDate(nextLine[4].equals("SYSDATE") ? LocalDate.now() :
                        LocalDate.parse(nextLine[4], formatter));
                employeeEntity.setBirthdayDate(nextLine[5].equals("SYSDATE") ? LocalDate.now() :
                        LocalDate.parse(nextLine[5], formatter));
                employeeEntity.setSalary(new BigDecimal(nextLine[6]));
                employeeEntity.setBonusPercent(new BigDecimal(nextLine[7]));
                employeeEntity.setMiddleName(nextLine[8].equals("") ? null : nextLine[8]);
                employeeEntity.setTelephoneNumber(nextLine[9].equals("") ? null : nextLine[9]);
                employeeEntity.setEmail(nextLine[10].equals("") ? null : nextLine[10]);
                employeeEntity.setDismissalDate(nextLine[11].equals("") ? null :
                        nextLine[11].equals("SYSDATE") ? LocalDate.now() :
                                LocalDate.parse(nextLine[11], formatter));
                toSave.add(employeeEntity);
            }
            employeesService.addAllEmployees(toSave);
            return EMPLOYEES_SUCCESS_MESSAGE;
        }
    }
}
