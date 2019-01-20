package ru.otus.akn.project.filters;

import ru.otus.akn.project.db.entity.DepartmentEntity;
import ru.otus.akn.project.db.entity.EmployeeEntity;
import ru.otus.akn.project.db.entity.PositionEntity;
import ru.otus.akn.project.db.entity.UserEntity;
import ru.otus.akn.project.util.EntityManagerControl;
import ru.otus.akn.project.util.EntityManagerControlGeneric;
import ru.otus.akn.project.xml.data.DepartmentsList;
import ru.otus.akn.project.xml.data.EmployeesList;
import ru.otus.akn.project.xml.data.PositionsList;
import ru.otus.akn.project.xml.data.UsersList;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Boolean.TRUE;
import static ru.otus.akn.project.db.dao.DepartmentsDAO.*;
import static ru.otus.akn.project.db.dao.EmployeesDAO.*;
import static ru.otus.akn.project.db.dao.PositionsDAO.*;
import static ru.otus.akn.project.db.dao.UsersDAO.*;
import static ru.otus.akn.project.util.PersistenceUtil.MANAGER_FACTORY;
import static ru.otus.akn.project.util.ResourceUtil.getFileAsBufferedReader;
import static ru.otus.akn.project.util.ResourceUtil.getFileAsBufferedWriter;

@WebListener
public class ApplicationServletContextListener implements ServletContextListener {

    private final static Logger LOGGER = Logger.getLogger(ApplicationServletContextListener.class.getName());
    private final static String PATH_TO_DEPARTMENT_FILE = "/WEB-INF/classes/xml-data/DepartmentData.xml";
    private final static String PATH_TO_EMPLOYEE_FILE = "/WEB-INF/classes/xml-data/EmployeeData.xml";
    private final static String PATH_TO_POSITION_FILE = "/WEB-INF/classes/xml-data/PositionData.xml";
    private final static String PATH_TO_USERS_FILE = "/WEB-INF/classes/xml-data/UsersData.xml";

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        ServletContext servletContext = servletContextEvent.getServletContext();

        boolean departmentOrPositionLoadingFailed = false;

        try {
            DepartmentsList departmentsList = unmarshallEntities(PATH_TO_DEPARTMENT_FILE, DepartmentsList.class, servletContext);
            saveAllDepartments(departmentsList.getDepartmentEntities());
        } catch (Throwable throwable) {
            departmentOrPositionLoadingFailed = true;
            LOGGER.log(Level.WARNING, "Got some problem when tried to unmarchall and upload objects", throwable);
        }

        try {
            PositionsList positionsList = unmarshallEntities(PATH_TO_POSITION_FILE, PositionsList.class, servletContext);
            saveAllPositions(positionsList.getPositionEntities());
        } catch (Throwable throwable) {
            departmentOrPositionLoadingFailed = true;
            LOGGER.log(Level.WARNING, "Got some problem when tried to unmarchall and upload objects", throwable);
        }

        if (!departmentOrPositionLoadingFailed) {
            try {
                EmployeesList employeesList = unmarshallEntities(PATH_TO_EMPLOYEE_FILE, EmployeesList.class, servletContext);
                saveAllEmployees(employeesList.getEmployeeEntities(), true);
            } catch (Throwable throwable) {
                LOGGER.log(Level.WARNING, "Got some problem when tried to unmarchall and upload objects", throwable);
            }
        }

        try {
            UsersList usersList = unmarshallEntities(PATH_TO_USERS_FILE, UsersList.class, servletContext);
            saveAllUsers(usersList.getUserEntities());
        } catch (Throwable throwable) {
            LOGGER.log(Level.WARNING, "Got some problem when tried to unmarchall and upload objects", throwable);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

        try {

            List<DepartmentEntity> departmentEntities = new ArrayList<>();
            List<PositionEntity> positionEntities = new ArrayList<>();
            List<EmployeeEntity> employeeEntities = new ArrayList<>();
            List<UserEntity> userEntities = new ArrayList<>();

            fillUpDepartmentEntities(departmentEntities);
            fillUpPositionEntity(positionEntities);
            fillUpEmployeeList(employeeEntities);
            fillUpUserEntities(userEntities);

            ServletContext servletContext = servletContextEvent.getServletContext();

            if (!employeeEntities.isEmpty()) {
                EmployeesList employeesList = new EmployeesList();
                employeesList.setEmployeeEntities(employeeEntities);
                marshallEntities(PATH_TO_EMPLOYEE_FILE, EmployeesList.class, servletContext, employeesList);
                deleteAllEmployees();
            }

            if (!userEntities.isEmpty()) {
                UsersList usersList = new UsersList();
                usersList.setUserEntities(userEntities);
                marshallEntities(PATH_TO_USERS_FILE, UsersList.class, servletContext, usersList);
                deleteAllUsers();
            }

            if (!positionEntities.isEmpty()) {
                PositionsList positionsList = new PositionsList();
                positionsList.setPositionEntities(positionEntities);
                marshallEntities(PATH_TO_POSITION_FILE, PositionsList.class, servletContext, positionsList);
                deleteAllPositions();
            }

            if (!departmentEntities.isEmpty()) {
                DepartmentsList departmentsList = new DepartmentsList();
                departmentsList.setDepartmentEntities(departmentEntities);
                marshallEntities(PATH_TO_DEPARTMENT_FILE, DepartmentsList.class, servletContext, departmentsList);
                deleteAllDepartments();
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

    private void fillUpUserEntities(List<UserEntity> userEntities) {
        try {
            userEntities.addAll(new EntityManagerControlGeneric<List<UserEntity>>(MANAGER_FACTORY) {
                @Override
                public List<UserEntity> requestMethod(EntityManager manager) {
                    return getAllUsersEntities(manager);
                }
            }.processRequest());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Got some problem when tried to get user objects from DB", e);
        }
    }

    private void deleteAllUsers() {
        try {
            new EntityManagerControl(MANAGER_FACTORY) {
                @Override
                public void requestMethod(EntityManager manager) {
                    deleteAllUsersEntities(manager);
                }
            }.processRequest();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Got some problem when tried to delete users from DB", e);
        }
    }

    private void fillUpPositionEntity(List<PositionEntity> positionEntities) {
        try {
            positionEntities.addAll(new EntityManagerControlGeneric<List<PositionEntity>>(MANAGER_FACTORY) {
                @Override
                public List<PositionEntity> requestMethod(EntityManager manager) {
                    return getAllPositionEntities(manager);
                }
            }.processRequest());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Got some problem when tried to get position objects from DB", e);
        }
    }

    private void deleteAllPositions() {
        try {
            new EntityManagerControl(MANAGER_FACTORY) {
                @Override
                public void requestMethod(EntityManager manager) {
                    deleteAllPositionsEntities(manager);
                }
            }.processRequest();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Got some problem when tried to delete positions from DB", e);
        }
    }

    private void fillUpDepartmentEntities(List<DepartmentEntity> departmentEntities) {
        try {
            departmentEntities.addAll(new EntityManagerControlGeneric<List<DepartmentEntity>>(MANAGER_FACTORY) {
                @Override
                public List<DepartmentEntity> requestMethod(EntityManager manager) {
                    return getAllDepartmentEntities(manager);
                }
            }.processRequest());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Got some problem when tried to get department objects from DB", e);
        }
    }

    private void deleteAllDepartments() {
        try {
            new EntityManagerControl(MANAGER_FACTORY) {
                @Override
                public void requestMethod(EntityManager manager) {
                    deleteAllDepartmentsEntities(manager);
                }
            }.processRequest();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Got some problem when tried to delete departments from DB", e);
        }
    }

    private void fillUpEmployeeList(List<EmployeeEntity> employeeEntities) {
        try {
            employeeEntities.addAll(new EntityManagerControlGeneric<List<EmployeeEntity>>(MANAGER_FACTORY) {
                @Override
                public List<EmployeeEntity> requestMethod(EntityManager manager) {
                    return getAllEmployeeEntitiesOrderById(manager);
                }
            }.processRequest());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Got some problem when tried to get employee objects from DB", e);
        }
    }

    private void deleteAllEmployees() {
        try {
            new EntityManagerControl(MANAGER_FACTORY) {
                @Override
                public void requestMethod(EntityManager manager) {
                    deleteAllEmployeeEntities(manager);
                }
            }.processRequest();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Got some problem when tried to delete employees from DB", e);
        }
    }
}
