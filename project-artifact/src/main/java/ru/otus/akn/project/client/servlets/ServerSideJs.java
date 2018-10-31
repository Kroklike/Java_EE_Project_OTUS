package ru.otus.akn.project.client.servlets;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/serverSideJs")
public class ServerSideJs extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
        ScriptEngine engine = factory.getScriptEngine("-scripting");

        try (PrintWriter pw = resp.getWriter()) {
            String programForExecution = req.getParameter("jsForExecution");
            pw.println(engine.eval(programForExecution));
        } catch (ScriptException e) {
            throw new RuntimeException("Something went wrong when tried to execute js on server side");
        }
    }
}
