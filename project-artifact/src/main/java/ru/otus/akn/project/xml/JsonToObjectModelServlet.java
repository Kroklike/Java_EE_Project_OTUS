package ru.otus.akn.project.xml;

import ru.otus.akn.project.db.entity.EmployeeEntity;
import ru.otus.akn.project.xml.data.EmployeeListFromJson;
import ru.otus.akn.project.xml.data.EmployeesList;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

import static ru.otus.akn.project.util.FileUtils.getWholeStringFromFile;
import static ru.otus.akn.project.util.ResourceUtil.getResourceFile;
import static ru.otus.akn.project.xml.XmlToJsonServlet.PATH_TO_JSON_FILE;

@WebServlet("/jsonToObjectModel")
public class JsonToObjectModelServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try (PrintWriter pw = resp.getWriter();
             BufferedReader fileReader = new BufferedReader(
                     new FileReader(getResourceFile(this.getServletContext(), PATH_TO_JSON_FILE)))) {

            pw.println("Path to json file: " + PATH_TO_JSON_FILE);
            String jsonContent = getWholeStringFromFile(fileReader).toString();
            if (jsonContent.isEmpty()) {
                throw new RuntimeException("Json file is empty.");
            }

            Jsonb jsonb = JsonbBuilder.create();
            EmployeesList result = jsonb.fromJson(jsonContent, EmployeeListFromJson.class).getEmployeesList();
            List<EmployeeEntity> resultList = result.getEmployeeEntities()
                    .stream().filter(element -> element.getEmployeeId() % 2 != 0)
                    .collect(Collectors.toList());

            if (resultList.isEmpty()) {
                pw.println("\nDid not find employees with odd ids");
                return;
            }

            pw.println("\nEmployees with odd ids: \n");
            for (EmployeeEntity employeeEntity : resultList) {
                pw.println(employeeEntity.toString());
            }

        } catch (Exception e) {
            throw new ServletException("Got exception when tried to get object from json", e);
        }
    }
}
