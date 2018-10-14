package ru.otus.akn.project.db.util;

import javax.servlet.GenericServlet;
import javax.servlet.ServletContext;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class ResourceUtil {

    public static File getResourceFile(GenericServlet servlet, String csvPath) throws MalformedURLException, URISyntaxException {
        ServletContext context = servlet.getServletContext();
        URL positionCsv = context.getResource(csvPath);
        return new File(positionCsv.toURI());
    }

}
