package ru.otus.akn.project.soap.zscore;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@WebServlet(name = "BankZScoreServiceClientServlet", urlPatterns = "/bankZScoreClient")
public class BankZScoreServiceClientServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        URL url = new URL(req.getScheme() + "://" + req.getServerName() + ":8785"
                + req.getContextPath() + "/bankZScore");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        StringBuilder jsonBuffer = new StringBuilder();
        try (Scanner scanner = new Scanner(conn.getInputStream())) {
            while (scanner.hasNextLine()) {
                jsonBuffer.append(scanner.nextLine());
            }
            JSONArray payload = new JSONArray(jsonBuffer.toString());
            PrintWriter writer = resp.getWriter();
            writer.println("Russian Banks's Z-Score by years:");
            for (int i = 0; i < payload.length(); i++) {
                JSONObject score = (JSONObject) payload.get(i);
                writer.println("Year: " + score.get("date") + " | Z-Score: " + score.get("value"));
            }
        }
    }
}
