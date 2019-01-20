package ru.otus.akn.project.util;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;
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

    public static void parseInnerCurrency(Node node, List<String> curNames, Map<String, BigDecimal> result) {
        NodeList childNodes = node.getChildNodes();
        boolean needToAdd = false;
        String curName = null;
        BigDecimal curRate = null;
        for (int j = 0; j < childNodes.getLength(); j++) {
            Node childItem = childNodes.item(j);
            if (childItem.getNodeName().equals("CharCode")) {
                String charCodeValue = childItem.getFirstChild().getTextContent();
                if (curNames.contains(charCodeValue)) {
                    needToAdd = true;
                    curName = charCodeValue;
                }
            }
            if (childItem.getNodeName().equals("Value")) {
                String rateValue = childItem.getFirstChild().getTextContent()
                        .replace("\"", "")
                        .replace(",", ".");
                curRate = new BigDecimal(rateValue);
            }
        }
        if (needToAdd) {
            result.put(curName, curRate);
        }
    }
}
