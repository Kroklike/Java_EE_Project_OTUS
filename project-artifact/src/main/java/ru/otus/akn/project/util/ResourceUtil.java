package ru.otus.akn.project.util;

import javax.servlet.ServletContext;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class ResourceUtil {

    public static File getResourceFile(ServletContext servletContext, String csvPath) throws MalformedURLException, URISyntaxException {
        URL positionCsv = servletContext.getResource(csvPath);
        return new File(positionCsv.toURI());
    }

}
