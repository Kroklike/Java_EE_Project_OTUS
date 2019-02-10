package ru.otus.akn.project.db;

import ru.otus.akn.project.db.entity.EmployeeEntity;
import ru.otus.akn.project.ejb.api.stateless.EmployeesService;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@WebServlet("/getEmployeesWithMaxSalary")
public class GetEmployeesWithMaxSalaryServlet extends HttpServlet {

    @EJB
    private EmployeesService employeesService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            try (PrintWriter pw = response.getWriter()) {
                for (EmployeeEntity employeeEntity : employeesService.getEmployeeEntitiesWithMaxSalary()) {
                    pw.print("{EmployeeId: " + employeeEntity.getEmployeeId() + " ");
                    pw.print("PositionName: " + employeeEntity.getPositionEntity().getPositionName() + " ");
                    pw.print("DepartmentName: " + employeeEntity.getDepartmentEntity().getDepartmentName() + " ");
                    pw.print("FirstName: " + employeeEntity.getFirstName() + " ");
                    pw.print("LastName: " + employeeEntity.getLastName() + " ");
                    pw.print("EmploymentDate: " + employeeEntity.getEmploymentDate() + " ");
                    pw.print("Salary: " + employeeEntity.getSalary() + " ");
                    pw.print("BonusPercent: " + employeeEntity.getBonusPercent() + " ");
                    pw.print("MiddleName: " + employeeEntity.getMiddleName() + " ");
                    pw.print("TelephoneNumber: " + employeeEntity.getTelephoneNumber() + " ");
                    pw.print("Email: " + employeeEntity.getEmail() + " ");
                    pw.print("DismissalDate: " + employeeEntity.getDismissalDate() + " ");
                    pw.print("UserLogin: " + employeeEntity.getUserEntity().getLogin() + " ");
                    pw.print("BirthdayDate: " + employeeEntity.getBirthdayDate() + "}\n");
                }
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
