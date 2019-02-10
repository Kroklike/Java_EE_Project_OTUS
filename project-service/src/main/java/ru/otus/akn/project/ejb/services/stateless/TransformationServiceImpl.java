package ru.otus.akn.project.ejb.services.stateless;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import ru.otus.akn.project.db.entity.DepartmentEntity;
import ru.otus.akn.project.db.entity.EmployeeEntity;
import ru.otus.akn.project.db.entity.PositionEntity;
import ru.otus.akn.project.ejb.api.stateless.EmployeesService;
import ru.otus.akn.project.ejb.api.stateless.TransformationService;
import ru.otus.akn.project.util.FileUtils;
import ru.otus.akn.project.util.ResourceUtil;
import ru.otus.akn.project.xml.data.EmployeeListFromJson;
import ru.otus.akn.project.xml.data.EmployeesList;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.servlet.ServletContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Boolean.TRUE;

@Stateless
public class TransformationServiceImpl implements TransformationService {

    private final static String PATH_TO_XML_FILE = "/WEB-INF/classes/xml-data/employee.xml";
    private static final String PATH_TO_JSON_FILE = "/WEB-INF/classes/json-data/employee.json";
    private static final String EMPLOYEES_JSON_NAME = "employees";
    private static final String EMPLOYEE_JSON_ARRAY = "employee";
    private static final int INDENT_FACTOR = 4;

    @EJB
    private EmployeesService employeesService;

    @Override
    public StringBuilder marshallXML(ServletContext servletContext) {
        List<EmployeeEntity> entitiesOrderById = employeesService.getAllEmployeeEntitiesOrderById();
        //If nothing found then add test subject
        if (entitiesOrderById.isEmpty()) {
            entitiesOrderById.add(getTestEmployeeEntity());
        }

        EmployeesList toMarshal = new EmployeesList();
        toMarshal.setEmployeeEntities(entitiesOrderById);

        StringBuilder result = new StringBuilder();

        try (BufferedWriter fileWriter = ResourceUtil.getFileAsBufferedWriter(servletContext, PATH_TO_XML_FILE);
             BufferedReader fileReader = ResourceUtil.getFileAsBufferedReader(servletContext, PATH_TO_XML_FILE)) {
            JAXBContext context = JAXBContext.newInstance(EmployeesList.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, TRUE);
            result.append("Path to file: " + new URI(PATH_TO_XML_FILE).toString() + "\n");
            marshaller.marshal(toMarshal, fileWriter);
            result.append("Employees list wrote successfully in xml format.\n");
            result.append("\nNow employee.xml file contains: \n\n");
            result.append(FileUtils.getWholeStringFromFile(fileReader) + "\n");
        } catch (Exception e) {
            throw new RuntimeException("Got exception when tried to marshal employee objects", e);
        }

        return result;
    }

    @Override
    public StringBuilder XMLToJson(ServletContext servletContext) {
        StringBuilder result = new StringBuilder();

        try (BufferedWriter writerJson = ResourceUtil.getFileAsBufferedWriter(servletContext, PATH_TO_JSON_FILE);
             BufferedReader fileReaderJson = ResourceUtil.getFileAsBufferedReader(servletContext, PATH_TO_JSON_FILE);
             BufferedReader fileReaderXml = ResourceUtil.getFileAsBufferedReader(servletContext, PATH_TO_XML_FILE)) {

            result.append("Path to xml file: " + PATH_TO_XML_FILE + "\n");
            result.append("Path to json file: " + PATH_TO_JSON_FILE + "\n");
            String xmlContent = FileUtils.getWholeStringFromFile(fileReaderXml).toString();
            if (xmlContent.isEmpty()) {
                throw new RuntimeException("Xml file is empty.");
            }
            JSONObject xmlJsonObj = XML.toJSONObject(xmlContent);
            JSONObject employeesObject = xmlJsonObj.getJSONObject(EMPLOYEES_JSON_NAME);
            JSONObject employeeArray = employeesObject.getJSONObject(EMPLOYEE_JSON_ARRAY);
            replaceSingleElementToArrayIfNeed(employeeArray, employeesObject);
            writerJson.write(xmlJsonObj.toString(INDENT_FACTOR));
            writerJson.flush();
            result.append("Employees list wrote successfully in json format.\n");
            result.append("\nNow employee.json file contains: \n\n");
            result.append(FileUtils.getWholeStringFromFile(fileReaderJson).toString() + "\n");
        } catch (Exception e) {
            throw new RuntimeException("Got exception when tried to convert xml to json", e);
        }

        return result;
    }

    @Override
    public StringBuilder XPathSearching(ServletContext servletContext) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;

        StringBuilder result = new StringBuilder();

        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(ResourceUtil.getFileAsInputStream(servletContext, PATH_TO_XML_FILE));

            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xPath = xPathFactory.newXPath();
            XPathExpression searchAverageSalary = xPath.compile(
                    "sum(/employees/employee/salary) div count(/employees/employee)");
            BigDecimal averageSalary = new BigDecimal(searchAverageSalary.evaluate(doc))
                    .setScale(4, BigDecimal.ROUND_HALF_UP);

            XPathExpression searchEmpWithMoreThanAvgSalary = xPath.compile("/employees/employee" +
                    "[salary > " + averageSalary + "]/@employeeId");

            NodeList resultNode = (NodeList) searchEmpWithMoreThanAvgSalary.evaluate(doc, XPathConstants.NODESET);

            result.append("Employees with more than average salary (" + averageSalary + "):\n");
            for (int i = 0; i < resultNode.getLength(); i++) {
                result.append("-EmployeeId: " + resultNode.item(i).getNodeValue() + "\n");
            }
        } catch (Exception e) {
            throw new RuntimeException("Got exception when tried use xpath.", e);
        }

        return result;
    }

    @Override
    public StringBuilder JsonToObjectModel(ServletContext servletContext) {

        StringBuilder result = new StringBuilder();

        try (BufferedReader fileReader = ResourceUtil.getFileAsBufferedReader(servletContext, PATH_TO_JSON_FILE)) {

            result.append("Path to json file: " + PATH_TO_JSON_FILE + "\n");
            String jsonContent = FileUtils.getWholeStringFromFile(fileReader).toString();
            if (jsonContent.isEmpty()) {
                throw new RuntimeException("Json file is empty.");
            }

            Jsonb jsonb = JsonbBuilder.create();
            EmployeesList employeesList = jsonb.fromJson(jsonContent, EmployeeListFromJson.class).getEmployeesList();
            List<EmployeeEntity> resultList = employeesList.getEmployeeEntities()
                    .stream().filter(element -> element.getEmployeeId() % 2 != 0)
                    .collect(Collectors.toList());

            if (resultList.isEmpty()) {
                result.append("\nDid not find employees with odd ids\n");
                return result;
            }

            result.append("\nEmployees with odd ids: \n");
            for (EmployeeEntity employeeEntity : resultList) {
                result.append(employeeEntity.toString() + "\n");
            }

        } catch (Exception e) {
            throw new RuntimeException("Got exception when tried to get object from json", e);
        }

        return result;
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

    private void replaceSingleElementToArrayIfNeed(Object InsideArray, JSONObject rootObject) {
        if (!(InsideArray instanceof JSONArray)) {
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(InsideArray);
            rootObject.remove(EMPLOYEE_JSON_ARRAY);
            rootObject.put(EMPLOYEE_JSON_ARRAY, jsonArray);
        }
    }
}
