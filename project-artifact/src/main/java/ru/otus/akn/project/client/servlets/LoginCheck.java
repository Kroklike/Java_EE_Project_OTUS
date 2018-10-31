package ru.otus.akn.project.client.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/loginCheck")
public class LoginCheck extends HttpServlet {

    private static final String failedLoginPage = "/html/login_failed.html";
    private static final String successLoginPage = "/html/login_success.html";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (username == null || username.isEmpty() ||
                username.matches(".+[\\W]+.+") || username.length() > 20 ||
                password == null || password.isEmpty()) {
            redirectToPage(resp, failedLoginPage);
        } else {
            redirectToPage(resp, successLoginPage);
        }
    }

    private void redirectToPage(HttpServletResponse resp, String url) throws IOException {
        resp.sendRedirect(url);
    }
}
