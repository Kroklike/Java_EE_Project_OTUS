package ru.otus.akn.project.xml;

import ru.otus.akn.project.ejb.api.stateless.TransformationService;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@WebServlet("/xmlToJsonConvert")
public class XmlToJsonServlet extends HttpServlet {

    @EJB
    private TransformationService transformationService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try (PrintWriter pw = resp.getWriter()) {
            pw.println(transformationService.XMLToJson(this.getServletContext()));
        } catch (Exception e) {
            throw new ServletException("Got exception when tried to convert xml to json", e);
        }
    }
}
