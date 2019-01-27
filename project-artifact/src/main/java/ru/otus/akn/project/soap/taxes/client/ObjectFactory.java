
package ru.otus.akn.project.soap.taxes.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ru.otus.akn.project.soap.taxes.client package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _CalculateTaxes_QNAME = new QName("http://taxes.soap.project.akn.otus.ru/", "calculateTaxes");
    private final static QName _CalculateTaxesResponse_QNAME = new QName("http://taxes.soap.project.akn.otus.ru/", "calculateTaxesResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ru.otus.akn.project.soap.taxes.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CalculateTaxes }
     * 
     */
    public CalculateTaxes createCalculateTaxes() {
        return new CalculateTaxes();
    }

    /**
     * Create an instance of {@link CalculateTaxesResponse }
     * 
     */
    public CalculateTaxesResponse createCalculateTaxesResponse() {
        return new CalculateTaxesResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CalculateTaxes }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://taxes.soap.project.akn.otus.ru/", name = "calculateTaxes")
    public JAXBElement<CalculateTaxes> createCalculateTaxes(CalculateTaxes value) {
        return new JAXBElement<CalculateTaxes>(_CalculateTaxes_QNAME, CalculateTaxes.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CalculateTaxesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://taxes.soap.project.akn.otus.ru/", name = "calculateTaxesResponse")
    public JAXBElement<CalculateTaxesResponse> createCalculateTaxesResponse(CalculateTaxesResponse value) {
        return new JAXBElement<CalculateTaxesResponse>(_CalculateTaxesResponse_QNAME, CalculateTaxesResponse.class, null, value);
    }

}
