package ru.otus.akn.project.ejb.api.singleton;

import javax.ejb.Remote;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@Remote
public interface RatesService {

    Map<String, BigDecimal> getRatesOrNull() throws IOException, XPathExpressionException;

    Map<String, BigDecimal> getRates() throws IOException, XPathExpressionException;

}
