package ru.otus.akn.project.util;

import javax.servlet.ServletContext;
import java.io.*;

public class ResourceUtil {

    public static BufferedReader getFileAsBufferedReader(ServletContext servletContext, String csvPath) throws IOException {
        return new BufferedReader(
                new InputStreamReader
                        (servletContext.getResource(csvPath).openConnection().getInputStream()));
    }

    public static BufferedWriter getFileAsBufferedWriter(ServletContext servletContext, String csvPath) throws IOException {
        return new BufferedWriter(
                new OutputStreamWriter
                        (servletContext.getResource(csvPath).openConnection().getOutputStream()));
    }

    public static InputStream getFileAsInputStream(ServletContext servletContext, String csvPath) throws IOException {
        return servletContext.getResource(csvPath).openConnection().getInputStream();
    }

}
