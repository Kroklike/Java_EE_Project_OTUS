package ru.otus.akn.project.xml;

import ru.otus.akn.project.db.entity.DepartmentEntity;
import ru.otus.akn.project.db.entity.EmployeeEntity;
import ru.otus.akn.project.db.entity.PositionEntity;
import ru.otus.akn.project.util.EntityManagerControlGeneric;
import ru.otus.akn.project.xml.data.EmployeesList;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Boolean.TRUE;
import static ru.otus.akn.project.db.dao.EmployeesDAO.getAllEmployeeEntitiesOrderById;
import static ru.otus.akn.project.util.FileUtils.getWholeStringFromFile;
import static ru.otus.akn.project.util.PersistenceUtil.MANAGER_FACTORY;
import static ru.otus.akn.project.util.ResourceUtil.getFileAsBufferedReader;
import static ru.otus.akn.project.util.ResourceUtil.getFileAsBufferedWriter;

@WebServlet("/marshalEmployees")
public class MarshalXMLServlet extends HttpServlet {

    public final static String PATH_TO_XML_FILE = "/WEB-INF/classes/xml-data/employee.xml";
    private final static Logger LOGGER = Logger.getLogger(MarshalXMLServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        List<EmployeeEntity> entitiesOrderById = new ArrayList<>();
        try {
            entitiesOrderById.addAll(new EntityManagerControlGeneric<List<EmployeeEntity>>(MANAGER_FACTORY) {
                @Override
                public List<EmployeeEntity> requestMethod(EntityManager manager) {
                    return getAllEmployeeEntitiesOrderById(manager);
                }
            }.processRequest());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Got some problem when tried to get employee objects from DB", e);
        }

        //If nothing found then add test subject
        if (entitiesOrderById.isEmpty()) {
            entitiesOrderById.add(getTestEmployeeEntity());
        }

        EmployeesList toMarshal = new EmployeesList();
        toMarshal.setEmployeeEntities(entitiesOrderById);

        try (PrintWriter pw = resp.getWriter();
             BufferedWriter fileWriter = getFileAsBufferedWriter(this.getServletContext(), PATH_TO_XML_FILE);
             BufferedReader fileReader = getFileAsBufferedReader(this.getServletContext(), PATH_TO_XML_FILE)) {
            JAXBContext context = JAXBContext.newInstance(EmployeesList.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, TRUE);
            pw.println("Path to file: " + new URI(PATH_TO_XML_FILE).toString());
            marshaller.marshal(toMarshal, fileWriter);
            pw.println("Employees list wrote successfully in xml format.");
            pw.println("\nNow employee.xml file contains: \n");
            pw.println(getWholeStringFromFile(fileReader));
        } catch (Exception e) {
            throw new ServletException("Got exception when tried to marshal employee objects", e);
        }
    }

    private EmployeeEntity getTestEmployeeEntity() {
        EmployeeEntity testEmployee = new EmployeeEntity();
        PositionEntity testPosition = new PositionEntity();
        testPosition.setPositionName("Main boss");
        testPosition.setPositionId(1L);
        DepartmentEntity testDepartment = new DepartmentEntity();
        testDepartment.setDepartmentName("Main department");
        testDepartment.setDepartmentId(1L);
        testEmployee.setPositionEntity(testPosition);
        testEmployee.setDepartmentEntity(testDepartment);
        testEmployee.setLastName("Boss");
        testEmployee.setFirstName("Ben");
        testEmployee.setSalary(new BigDecimal(20000));
        testEmployee.setEmployeeId(1L);
        testEmployee.setBonusPercent(new BigDecimal(30));
        testEmployee.setEmploymentDate(LocalDate.now());
        testEmployee.setBirthdayDate(LocalDate.now());
        return testEmployee;
    }
}
