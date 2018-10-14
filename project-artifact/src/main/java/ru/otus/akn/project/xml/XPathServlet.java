package ru.otus.akn.project.xml;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.PrintWriter;

import static ru.otus.akn.project.db.util.ResourceUtil.getResourceFile;
import static ru.otus.akn.project.xml.MarshalXMLServlet.FILE_TO_SAVE_EMPLOYEES_OBJECT;

@WebServlet("/xPathEmployeeSearching")
public class XPathServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;

        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(getResourceFile(this, FILE_TO_SAVE_EMPLOYEES_OBJECT));

            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xPath = xPathFactory.newXPath();
            XPathExpression expression = xPath.compile("/employees/employee" +
                    "[salary > (sum(/employees/employee/salary) div count(/employees/employee))]/@employeeId");

            NodeList result = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);

            try (PrintWriter pw = resp.getWriter()) {
                pw.println("Employees with more than average salary:");
                for (int i = 0; i < result.getLength(); i++) {
                    pw.println("-Employee number: " + result.item(i).getNodeValue());
                }
            }

        } catch (Exception e) {
            throw new ServletException("Got exception when tried use xpath.", e);
        }
    }
}
