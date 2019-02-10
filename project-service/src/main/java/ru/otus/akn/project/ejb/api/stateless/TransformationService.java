package ru.otus.akn.project.ejb.api.stateless;

import javax.ejb.Remote;
import javax.servlet.ServletContext;

@Remote
public interface TransformationService {

    StringBuilder marshallXML(ServletContext servletContext);

    StringBuilder XMLToJson(ServletContext servletContext);

    StringBuilder XPathSearching(ServletContext servletContext);

    StringBuilder JsonToObjectModel(ServletContext servletContext);

}
