package ru.otus.akn.project.util;

import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class OutputResultUtil {

    public static <T> void writeMapToResponse(HttpServletResponse resp, Map<String, T> toWrite) throws IOException {
        JSONObject json = new JSONObject();
        for (Map.Entry<String, T> entry : toWrite.entrySet()) {
            json.put(entry.getKey(), entry.getValue());
        }
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            out.write(json.toString());
        }
    }
}
