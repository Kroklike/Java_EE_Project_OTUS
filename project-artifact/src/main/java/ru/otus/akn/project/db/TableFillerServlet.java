package ru.otus.akn.project.db;

import com.opencsv.CSVReader;
import org.apache.commons.codec.digest.DigestUtils;
import ru.otus.akn.project.db.entity.DepartmentEntity;
import ru.otus.akn.project.db.entity.EmployeeEntity;
import ru.otus.akn.project.db.entity.PositionEntity;
import ru.otus.akn.project.db.entity.UserEntity;
import ru.otus.akn.project.util.EntityManagerControl;
import ru.otus.akn.project.util.TransactionQueryConsumer;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static ru.otus.akn.project.db.dao.DepartmentsDAO.getAllDepartmentEntities;
import static ru.otus.akn.project.db.dao.DepartmentsDAO.getDepartmentEntity;
import static ru.otus.akn.project.db.dao.EmployeesDAO.getAllEmployeeEntities;
import static ru.otus.akn.project.db.dao.PositionsDAO.getAllPositionEntities;
import static ru.otus.akn.project.db.dao.PositionsDAO.getPositionEntity;
import static ru.otus.akn.project.db.dao.UsersDAO.getAllUsersEntities;
import static ru.otus.akn.project.util.PersistenceUtil.MANAGER_FACTORY;
import static ru.otus.akn.project.util.ResourceUtil.getFileAsBufferedReader;

@WebServlet("/fillTable")
public class TableFillerServlet extends HttpServlet {

    private static final String CSV_DEPARTMENTS = "/WEB-INF/classes/DATA/DEPARTMENTS.csv";
    private static final String CSV_POSITIONS = "/WEB-INF/classes/DATA/POSITIONS.csv";
    private static final String CSV_EMPLOYEES = "/WEB-INF/classes/DATA/EMPLOYEES.csv";
    private static final String CSV_USERS = "/WEB-INF/classes/DATA/USERS.csv";
    private static final String DATE_FORMAT = "dd.MM.yyyy";
    private static final char CSV_SPLITTER = ';';

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            ServletContext servletContext = this.getServletContext();
            new EntityManagerControl(MANAGER_FACTORY) {
                @Override
                public void requestMethod(EntityManager manager) throws Exception {
                    fillUpDepartmentsTable(response, manager, servletContext);
                    fillUpPositionsTable(response, manager, servletContext);
                    fillUpEmployeesTable(response, manager, servletContext);
                    fillUpUsersTable(response, manager, servletContext);
                }
            }.processRequest();
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void fillUpDepartmentsTable(HttpServletResponse response, EntityManager em, ServletContext context) throws Exception {
        if (getAllDepartmentEntities(em).size() != 0) {
            response.getWriter().println("Departments table have already filled");
            return;
        }
        new TransactionQueryConsumer(em) {
            @Override
            public void needToProcessData() throws Exception {
                try (CSVReader reader = new CSVReader(getFileAsBufferedReader(context, CSV_DEPARTMENTS), CSV_SPLITTER)) {
                    String[] nextLine;
                    while ((nextLine = reader.readNext()) != null) {
                        DepartmentEntity departmentEntity = new DepartmentEntity();
                        departmentEntity.setDepartmentName(nextLine[0]);
                        departmentEntity.setCity(nextLine[1]);
                        departmentEntity.setFullAddress(nextLine[2]);
                        em.persist(departmentEntity);
                    }
                }
            }
        }.processQueryInTransaction();
        response.getWriter().println("Departments table filled");
    }

    private void fillUpUsersTable(HttpServletResponse response, EntityManager em, ServletContext servletContext) throws Exception {
        if (getAllUsersEntities(em).size() != 0) {
            response.getWriter().println("Users table have already filled");
            return;
        }
        new TransactionQueryConsumer(em) {
            @Override
            public void needToProcessData() throws Exception {
                try (CSVReader reader = new CSVReader(getFileAsBufferedReader(servletContext, CSV_USERS), CSV_SPLITTER)) {
                    String[] nextLine;
                    while ((nextLine = reader.readNext()) != null) {
                        UserEntity userEntity = new UserEntity();
                        userEntity.setLogin(nextLine[0]);
                        userEntity.setPasswordHash(DigestUtils.sha256Hex(nextLine[1]));
                        userEntity.setIsActive(Boolean.parseBoolean(nextLine[2]));
                        em.persist(userEntity);
                    }
                }
            }
        }.processQueryInTransaction();
        response.getWriter().println("Users table filled");
    }

    private void fillUpPositionsTable(HttpServletResponse response, EntityManager em, ServletContext servletContext) throws Exception {
        if (getAllPositionEntities(em).size() != 0) {
            response.getWriter().println("Positions table have already filled");
            return;
        }
        new TransactionQueryConsumer(em) {
            @Override
            public void needToProcessData() throws Exception {
                try (CSVReader reader = new CSVReader(getFileAsBufferedReader(servletContext, CSV_POSITIONS), CSV_SPLITTER)) {
                    String[] nextLine;
                    while ((nextLine = reader.readNext()) != null) {
                        PositionEntity positionEntity = new PositionEntity();
                        positionEntity.setPositionName(nextLine[0]);
                        em.persist(positionEntity);
                    }
                }
            }
        }.processQueryInTransaction();
        response.getWriter().println("Positions table filled");
    }

    private void fillUpEmployeesTable(HttpServletResponse response, EntityManager em, ServletContext servletContext) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        if (getAllEmployeeEntities(em).size() != 0) {
            response.getWriter().println("Employees table have already filled");
            return;
        }
        new TransactionQueryConsumer(em) {
            @Override
            public void needToProcessData() throws Exception {
                try (CSVReader reader = new CSVReader(getFileAsBufferedReader(servletContext, CSV_EMPLOYEES), CSV_SPLITTER)) {
                    String[] nextLine;
                    while ((nextLine = reader.readNext()) != null) {
                        if (nextLine.length == 0) {
                            continue;
                        }
                        EmployeeEntity employeeEntity = new EmployeeEntity();
                        employeeEntity.setPositionEntity(getPositionEntity(em, nextLine[0]));
                        employeeEntity.setDepartmentEntity(getDepartmentEntity(em, nextLine[1]));
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
                        em.persist(employeeEntity);
                    }
                }
            }
        }.processQueryInTransaction();
        response.getWriter().println("Employees table filled");
    }

}
