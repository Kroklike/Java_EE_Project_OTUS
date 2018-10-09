package ru.otus.akn.project.db;

import ru.otus.akn.project.db.util.EntityManagerControlGeneric;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

import static ru.otus.akn.project.db.util.PersistenceUtil.MANAGER_FACTORY;

@WebServlet("/getEmployeesWithMaxSalary")
public class GetEmployeesWithMaxSalaryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            List<Object[]> employeeList = new EntityManagerControlGeneric<List<Object[]>>(MANAGER_FACTORY) {
                @Override
                public List<Object[]> requestMethod(EntityManager manager) {
                    StoredProcedureQuery query = manager
                            .createStoredProcedureQuery("employee_with_max_salary")
                            .registerStoredProcedureParameter(1, Class.class,
                                    ParameterMode.REF_CURSOR);
                    query.execute();
                    return query.getResultList();
                }
            }.processRequest();

            try (PrintWriter pw = response.getWriter()) {
                for (Object[] employee : employeeList) {
                    pw.print("{EmployeeId: " + employee[0] + " ");
                    pw.print("PositionId: " + employee[1] + " ");
                    pw.print("DepartmentId: " + employee[2] + " ");
                    pw.print("FirstName: " + employee[3] + " ");
                    pw.print("LastName: " + employee[4] + " ");
                    pw.print("EmploymentDate: " + employee[5] + " ");
                    pw.print("Salary: " + employee[6] + " ");
                    pw.print("BonusPercent: " + employee[7] + " ");
                    pw.print("MiddleName: " + employee[8] + " ");
                    pw.print("TelephoneNumber: " + employee[9] + " ");
                    pw.print("Email: " + employee[10] + " ");
                    pw.print("DismissalDate: " + employee[11] + " ");
                    pw.print("UserId: " + employee[12] + "}\n");
                }
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
