package ru.otus.akn.project.xml;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import static ru.otus.akn.project.util.FileUtils.getWholeStringFromFile;
import static ru.otus.akn.project.util.ResourceUtil.getResourceFile;
import static ru.otus.akn.project.xml.MarshalXMLServlet.PATH_TO_XML_FILE;

@WebServlet("/xmlToJsonConvert")
public class XmlToJsonServlet extends HttpServlet {

    public static final String PATH_TO_JSON_FILE = "/WEB-INF/classes/json-data/employee.json";
    private static final String EMPLOYEES_JSON_NAME = "employees";
    private static final String EMPLOYEE_JSON_ARRAY = "employee";
    private static final int INDENT_FACTOR = 4;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try (PrintWriter pw = resp.getWriter();
             FileWriter fileWriterJson = new FileWriter(getResourceFile(this, PATH_TO_JSON_FILE));
             BufferedReader fileReaderJson = new BufferedReader(
                     new FileReader(getResourceFile(this, PATH_TO_JSON_FILE)));
             BufferedReader fileReaderXml = new BufferedReader(
                     new FileReader(getResourceFile(this, PATH_TO_XML_FILE)))) {

            pw.println("Path to xml file: " + PATH_TO_XML_FILE);
            pw.println("Path to json file: " + PATH_TO_JSON_FILE);
            String xmlContent = getWholeStringFromFile(fileReaderXml).toString();
            if (xmlContent.isEmpty()) {
                throw new RuntimeException("Xml file is empty.");
            }
            JSONObject xmlJsonObj = XML.toJSONObject(xmlContent);
            JSONObject employeesObject = xmlJsonObj.getJSONObject(EMPLOYEES_JSON_NAME);
            JSONObject employeeArray = employeesObject.getJSONObject(EMPLOYEE_JSON_ARRAY);
            replaceSingleElementToArrayIfNeed(employeeArray, employeesObject);
            fileWriterJson.write(xmlJsonObj.toString(INDENT_FACTOR));
            fileWriterJson.flush();
            pw.println("Employees list wrote successfully in json format.");
            pw.println("\nNow employee.json file contains: \n");
            pw.println(getWholeStringFromFile(fileReaderJson).toString());
        } catch (Exception e) {
            throw new ServletException("Got exception when tried to convert xml to json", e);
        }
    }

    private void replaceSingleElementToArrayIfNeed(Object InsideArray, JSONObject rootObject) {
        if (!(InsideArray instanceof JSONArray)) {
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(InsideArray);
            rootObject.remove(EMPLOYEE_JSON_ARRAY);
            rootObject.put(EMPLOYEE_JSON_ARRAY, jsonArray);
        }
    }

}
