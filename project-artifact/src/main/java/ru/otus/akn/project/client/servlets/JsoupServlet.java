package ru.otus.akn.project.client.servlets;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import ru.otus.akn.project.ejb.api.singleton.RBCService;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import static ru.otus.akn.project.util.OutputResultUtil.writeMapToResponse;

@WebServlet("/jsoupRBC")
public class JsoupServlet extends HttpServlet {

    @EJB
    private RBCService rbcService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setCharacterEncoding("UTF-8");
            String callbackParam = req.getParameter("callback");
            if (callbackParam != null) {
                jsonpResponse(resp, callbackParam);
            } else {
                writeMapToResponse(resp, rbcService.getFreshNews());
            }
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong when tried to get news from rbc.ru", e);
        }
    }

    private void jsonpResponse(HttpServletResponse resp,
                               String callback) throws JSONException, IOException {
        JSONArray array = new JSONArray();

        for (Map.Entry<String, String> entry : rbcService.getFreshNews().entrySet()) {
            JSONObject object = new JSONObject();
            object.append("title", entry.getKey());
            object.append("url", entry.getValue());
            array.put(object);
        }

        resp.setContentType("application/json; charset=utf-8");
        try (PrintWriter out = resp.getWriter()) {
            out.write(callback + "(" + array.toString() + ")");
        }
    }
}
