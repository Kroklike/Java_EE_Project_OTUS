package ru.otus.akn.project.db;

import javax.persistence.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

@WebServlet("/getEmployeesWithMaxSalary")
public class GetEmployeesWithMaxSalaryServlet extends HttpServlet {

    private static final String PERSISTENCE_UNIT_NAME = "jpa";
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME); // for Tomcat

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        EntityManager manager = emf.createEntityManager();
        try {
            StoredProcedureQuery query = manager
                    .createStoredProcedureQuery("employee_with_max_salary")
                    .registerStoredProcedureParameter(1, Class.class,
                            ParameterMode.REF_CURSOR);
            query.execute();
            List<Object[]> employeeList = query.getResultList();
            try (PrintWriter pw = response.getWriter()) {
                for (Object[] employee : employeeList) {
                    pw.println(Arrays.toString(employee));
                }
            }
        } catch (Exception e) {
            throw new ServletException(e);
        } finally {
            manager.close();
        }
    }
}
