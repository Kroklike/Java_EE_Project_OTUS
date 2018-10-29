package ru.otus.akn.project.util;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

public class Converter {

    public static Document convertStringWithXmlToDocument(String stringWithXml) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(stringWithXml)));
            return doc;
        } catch (Exception e) {
            throw new RuntimeException("Cannot parse string.", e);
        }
    }

}
