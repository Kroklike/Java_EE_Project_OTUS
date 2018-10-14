package ru.otus.akn.project.db;

import com.opencsv.CSVReader;
import ru.otus.akn.project.db.entity.DepartmentEntity;
import ru.otus.akn.project.db.entity.EmployeeEntity;
import ru.otus.akn.project.db.entity.PositionEntity;
import ru.otus.akn.project.db.util.EntityManagerControl;
import ru.otus.akn.project.db.util.TransactionQueryConsumer;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static ru.otus.akn.project.db.dao.DepartmentsDAO.getAllDepartmentEntities;
import static ru.otus.akn.project.db.dao.DepartmentsDAO.getDepartmentEntity;
import static ru.otus.akn.project.db.dao.EmployeesDAO.getAllEmployeeEntities;
import static ru.otus.akn.project.db.dao.PositionsDAO.getAllPositionEntities;
import static ru.otus.akn.project.db.dao.PositionsDAO.getPositionEntity;
import static ru.otus.akn.project.db.util.PersistenceUtil.MANAGER_FACTORY;
import static ru.otus.akn.project.db.util.ResourceUtil.getResourceFile;

@WebServlet("/fillTable")
public class TableFillerServlet extends HttpServlet {

    private static final String CSV_DEPARTMENTS = "/WEB-INF/classes/DATA/DEPARTMENTS.csv";
    private static final String CSV_POSITIONS = "/WEB-INF/classes/DATA/POSITIONS.csv";
    private static final String CSV_EMPLOYEES = "/WEB-INF/classes/DATA/EMPLOYEES.csv";
    private static final String DATE_FORMAT = "dd.MM.yyyy";
    private static final char CSV_SPLITTER = ';';

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            new EntityManagerControl(MANAGER_FACTORY) {
                @Override
                public void requestMethod(EntityManager manager) throws Exception {
                    fillUpDepartmentsTable(response, manager);
                    fillUpPositionsTable(response, manager);
                    fillUpEmployeesTable(response, manager);
                }
            }.processRequest();
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void fillUpDepartmentsTable(HttpServletResponse response, EntityManager em) throws Exception {
        if (getAllDepartmentEntities(em).size() != 0) {
            response.getWriter().println("Departments table have already filled");
            return;
        }
        File departmentFile = getResourceFile(this, CSV_DEPARTMENTS);
        new TransactionQueryConsumer(em) {
            @Override
            public void needToProcessData() throws Exception {
                try (CSVReader reader = new CSVReader(new FileReader(departmentFile), CSV_SPLITTER)) {
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

    private void fillUpPositionsTable(HttpServletResponse response, EntityManager em) throws Exception {
        if (getAllPositionEntities(em).size() != 0) {
            response.getWriter().println("Positions table have already filled");
            return;
        }
        File positionFile = getResourceFile(this, CSV_POSITIONS);
        new TransactionQueryConsumer(em) {
            @Override
            public void needToProcessData() throws Exception {
                try (CSVReader reader = new CSVReader(new FileReader(positionFile), CSV_SPLITTER)) {
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

    private void fillUpEmployeesTable(HttpServletResponse response, EntityManager em) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        if (getAllEmployeeEntities(em).size() != 0) {
            response.getWriter().println("Employees table have already filled");
            return;
        }
        File employeeFile = getResourceFile(this, CSV_EMPLOYEES);
        new TransactionQueryConsumer(em) {
            @Override
            public void needToProcessData() throws Exception {
                try (CSVReader reader = new CSVReader(new FileReader(employeeFile), CSV_SPLITTER)) {
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
                        employeeEntity.setSalary(new BigDecimal(nextLine[5]));
                        employeeEntity.setBonusPercent(new BigDecimal(nextLine[6]));
                        employeeEntity.setMiddleName(nextLine[7].equals("") ? null : nextLine[7]);
                        employeeEntity.setTelephoneNumber(nextLine[8].equals("") ? null : nextLine[8]);
                        employeeEntity.setEmail(nextLine[9].equals("") ? null : nextLine[9]);
                        employeeEntity.setDismissalDate(nextLine[10].equals("") ? null :
                                nextLine[10].equals("SYSDATE") ? LocalDate.now() :
                                        LocalDate.parse(nextLine[10], formatter));
                        em.persist(employeeEntity);
                    }
                }
            }
        }.processQueryInTransaction();
        response.getWriter().println("Employees table filled");
    }

}
