package ru.otus.akn.project.db;

import com.opencsv.CSVReader;
import ru.otus.akn.project.db.entity.DepartmentEntity;
import ru.otus.akn.project.db.entity.EmployeeEntity;
import ru.otus.akn.project.db.entity.PositionEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static ru.otus.akn.project.db.dao.DepartmentsDAO.getDepartmentEntity;
import static ru.otus.akn.project.db.dao.PositionDAO.getPositionEntity;

@WebServlet("/fillTable")
public class TableFillerServlet extends HttpServlet {

    private static final String PERSISTENCE_UNIT_NAME = "jpa";
    private static final String CSV_DEPARTMENTS = "/WEB-INF/classes/DATA/DEPARTMENTS.csv";
    private static final String CSV_POSITIONS = "/WEB-INF/classes/DATA/POSITIONS.csv";
    private static final String CSV_EMPLOYEES = "/WEB-INF/classes/DATA/EMPLOYEES.csv";
    private static final String DATE_FORMAT = "dd.MM.yyyy";
    private static final char CSV_SPLITTER = ';';

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME); // for Tomcat

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        EntityManager manager = emf.createEntityManager();
        try {
            fillUpDepartmentsTable(response, manager);
            fillUpPositionsTable(response, manager);
            fillUpEmployeesTable(response, manager);
        } finally {
            manager.close();
        }
    }

    private void fillUpDepartmentsTable(HttpServletResponse response, EntityManager em) throws ServletException {
        EntityTransaction transaction = em.getTransaction();
        try {
            File departmentFile = getResourceFile(CSV_DEPARTMENTS);
            transaction.begin();
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
            transaction.commit();
            response.getWriter().println("Departments table filled");
        } catch (Exception e) {
            transaction.rollback();
            throw new ServletException(e);
        }
    }

    private void fillUpPositionsTable(HttpServletResponse response, EntityManager em) throws ServletException {
        EntityTransaction transaction = em.getTransaction();
        try {
            File positionFile = getResourceFile(CSV_POSITIONS);
            transaction.begin();
            try (CSVReader reader = new CSVReader(new FileReader(positionFile), CSV_SPLITTER)) {
                String[] nextLine;
                while ((nextLine = reader.readNext()) != null) {
                    PositionEntity positionEntity = new PositionEntity();
                    positionEntity.setPositionName(nextLine[0]);
                    em.persist(positionEntity);
                }
            }
            transaction.commit();
            response.getWriter().println("Positions table filled");
        } catch (Exception e) {
            transaction.rollback();
            throw new ServletException(e);
        }
    }

    private void fillUpEmployeesTable(HttpServletResponse response, EntityManager em) throws ServletException {
        EntityTransaction transaction = em.getTransaction();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        try {
            File employeeFile = getResourceFile(CSV_EMPLOYEES);
            transaction.begin();
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
            transaction.commit();
            response.getWriter().println("Employees table filled");
        } catch (Exception e) {
            transaction.rollback();
            throw new ServletException(e);
        }
    }


    private File getResourceFile(String csvPath) throws MalformedURLException, URISyntaxException {
        ServletContext context = getServletContext();
        URL positionCsv = context.getResource(csvPath);
        return new File(positionCsv.toURI());
    }

}