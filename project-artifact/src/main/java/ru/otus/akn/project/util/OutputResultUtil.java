package ru.otus.akn.project.util;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class OutputResultUtil {

    public static <T> void writeMapToResponse(HttpServletResponse resp,
                                              Map<String, T> toWrite,
                                              String contentType) throws IOException, JSONException {
        JSONObject json = getJsonResultMap(toWrite);
        resp.setContentType(contentType);
        try (PrintWriter out = resp.getWriter()) {
            out.write(json.toString());
        }
    }

    public static <T> JSONObject getJsonResultMap(Map<String, T> toWrite) throws JSONException {
        JSONObject json = new JSONObject();
        for (Map.Entry<String, T> entry : toWrite.entrySet()) {
            json.append(entry.getKey(), entry.getValue());
        }
        return json;
    }

    public static <T> void writeMapToResponse(HttpServletResponse resp, Map<String, T> toWrite) throws IOException, JSONException {
        writeMapToResponse(resp, toWrite, "application/json");
    }
}
