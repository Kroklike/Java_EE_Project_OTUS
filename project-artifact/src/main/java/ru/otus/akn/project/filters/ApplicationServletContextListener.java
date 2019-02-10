package ru.otus.akn.project.filters;

import ru.otus.akn.project.db.entity.DepartmentEntity;
import ru.otus.akn.project.db.entity.EmployeeEntity;
import ru.otus.akn.project.db.entity.PositionEntity;
import ru.otus.akn.project.db.entity.UserEntity;
import ru.otus.akn.project.ejb.api.stateless.DepartmentsService;
import ru.otus.akn.project.ejb.api.stateless.EmployeesService;
import ru.otus.akn.project.ejb.api.stateless.PositionsService;
import ru.otus.akn.project.ejb.api.stateless.UsersService;
import ru.otus.akn.project.xml.data.DepartmentsList;
import ru.otus.akn.project.xml.data.EmployeesList;
import ru.otus.akn.project.xml.data.PositionsList;
import ru.otus.akn.project.xml.data.UsersList;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Boolean.TRUE;
import static ru.otus.akn.project.util.ResourceUtil.getFileAsBufferedReader;
import static ru.otus.akn.project.util.ResourceUtil.getFileAsBufferedWriter;

@WebListener
public class ApplicationServletContextListener implements ServletContextListener {

    private final static Logger LOGGER = Logger.getLogger(ApplicationServletContextListener.class.getName());
    private final static String PATH_TO_DEPARTMENT_FILE = "/WEB-INF/classes/xml-data/DepartmentData.xml";
    private final static String PATH_TO_EMPLOYEE_FILE = "/WEB-INF/classes/xml-data/EmployeeData.xml";
    private final static String PATH_TO_POSITION_FILE = "/WEB-INF/classes/xml-data/PositionData.xml";
    private final static String PATH_TO_USERS_FILE = "/WEB-INF/classes/xml-data/UsersData.xml";

    @EJB
    private DepartmentsService departmentsService;
    @EJB
    private PositionsService positionsService;
    @EJB
    private EmployeesService employeesService;
    @EJB
    private UsersService usersService;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        ServletContext servletContext = servletContextEvent.getServletContext();

        boolean departmentOrPositionLoadingFailed = false;

        try {
            DepartmentsList departmentsList = unmarshallEntities(PATH_TO_DEPARTMENT_FILE, DepartmentsList.class, servletContext);
            departmentsService.saveAllDepartments(departmentsList.getDepartmentEntities());
        } catch (Throwable throwable) {
            departmentOrPositionLoadingFailed = true;
            LOGGER.log(Level.WARNING, "Got some problem when tried to unmarchall and upload objects", throwable);
        }

        try {
            PositionsList positionsList = unmarshallEntities(PATH_TO_POSITION_FILE, PositionsList.class, servletContext);
            positionsService.saveAllPositions(positionsList.getPositionEntities());
        } catch (Throwable throwable) {
            departmentOrPositionLoadingFailed = true;
            LOGGER.log(Level.WARNING, "Got some problem when tried to unmarchall and upload objects", throwable);
        }

        if (!departmentOrPositionLoadingFailed) {
            try {
                EmployeesList employeesList = unmarshallEntities(PATH_TO_EMPLOYEE_FILE, EmployeesList.class, servletContext);
                employeesService.addAllEmployees(employeesList.getEmployeeEntities(), true);
            } catch (Throwable throwable) {
                LOGGER.log(Level.WARNING, "Got some problem when tried to unmarchall and upload objects", throwable);
            }
        }

        try {
            UsersList usersList = unmarshallEntities(PATH_TO_USERS_FILE, UsersList.class, servletContext);
            usersService.saveAllUsers(usersList.getUserEntities());
        } catch (Throwable throwable) {
            LOGGER.log(Level.WARNING, "Got some problem when tried to unmarchall and upload objects", throwable);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

        try {

            List<DepartmentEntity> departmentEntities = departmentsService.getAllDepartmentEntities();
            List<PositionEntity> positionEntities = positionsService.getAllPositionEntities();
            List<EmployeeEntity> employeeEntities = employeesService.getAllEmployeeEntitiesOrderById();
            List<UserEntity> userEntities = usersService.getAllUsersEntities();

            ServletContext servletContext = servletContextEvent.getServletContext();

            if (!employeeEntities.isEmpty()) {
                EmployeesList employeesList = new EmployeesList();
                employeesList.setEmployeeEntities(employeeEntities);
                marshallEntities(PATH_TO_EMPLOYEE_FILE, EmployeesList.class, servletContext, employeesList);
                employeesService.deleteAllEmployeeEntities();
            }

            if (!userEntities.isEmpty()) {
                UsersList usersList = new UsersList();
                usersList.setUserEntities(userEntities);
                marshallEntities(PATH_TO_USERS_FILE, UsersList.class, servletContext, usersList);
                usersService.deleteAllUsersEntities();
            }

            if (!positionEntities.isEmpty()) {
                PositionsList positionsList = new PositionsList();
                positionsList.setPositionEntities(positionEntities);
                marshallEntities(PATH_TO_POSITION_FILE, PositionsList.class, servletContext, positionsList);
                positionsService.deleteAllPositionsEntities();
            }

            if (!departmentEntities.isEmpty()) {
                DepartmentsList departmentsList = new DepartmentsList();
                departmentsList.setDepartmentEntities(departmentEntities);
                marshallEntities(PATH_TO_DEPARTMENT_FILE, DepartmentsList.class, servletContext, departmentsList);
                departmentsService.deleteAllDepartmentEntities();
            }

        } catch (Throwable throwable) {
            LOGGER.log(Level.WARNING, "Got some problem when tried to marchall and delete objects", throwable);
        }
    }

    private <T> void marshallEntities(String pathToFile, Class<T> tClass, ServletContext contextServlet, T listToSave) {
        try (BufferedWriter fileWriter = getFileAsBufferedWriter(contextServlet, pathToFile)) {
            JAXBContext context = JAXBContext.newInstance(tClass);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, TRUE);
            marshaller.marshal(listToSave, fileWriter);
        } catch (Exception e) {
            throw new RuntimeException("Got exception when tried to marshal objects", e);
        }
    }

    private <T> T unmarshallEntities(String pathToFile, Class<T> tClass, ServletContext contextServlet) {
        try (BufferedReader fileReader = getFileAsBufferedReader(contextServlet, pathToFile)) {
            JAXBContext context = JAXBContext.newInstance(tClass);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (T) unmarshaller.unmarshal(fileReader);
        } catch (Exception e) {
            throw new RuntimeException("Got exception when tried to unmarshal objects", e);
        }
    }
}
