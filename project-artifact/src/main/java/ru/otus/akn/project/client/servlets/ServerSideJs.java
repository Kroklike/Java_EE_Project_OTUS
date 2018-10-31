package ru.otus.akn.project.client.servlets;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.ScriptEngine;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/serverSideJs")
public class ServerSideJs extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
        ScriptEngine engine = factory.getScriptEngine("-scripting");

        try {
            String programForExecution = req.getParameter("jsForExecution");
            engine.eval(programForExecution);
            resp.sendRedirect("/html/server_side_js.html");
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong when tried to execute js on server side", e);
        }
    }
}
