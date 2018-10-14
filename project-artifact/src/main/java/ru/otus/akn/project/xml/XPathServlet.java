package ru.otus.akn.project.xml;

import org.w3c.dom.Document;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;

import static ru.otus.akn.project.xml.MarshalXMLServlet.FILE_TO_SAVE_EMPLOYEES_OBJECT;

@WebServlet("/xPathEmployeeSearching")
public class XPathServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc = null;

        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(FILE_TO_SAVE_EMPLOYEES_OBJECT);



        } catch (Exception e) {
            throw new ServletException("Got exception when tried use xpath.", e);
        }
    }
}
