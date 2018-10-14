package ru.otus.akn.project.util;

import java.io.BufferedReader;
import java.io.IOException;

public class FileUtils {

    public static StringBuilder getWholeStringFromFile(BufferedReader reader) throws IOException {
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line).append("\n");
        }
        return builder;
    }

}
