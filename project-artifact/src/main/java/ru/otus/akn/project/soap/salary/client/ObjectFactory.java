
package ru.otus.akn.project.soap.salary.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ru.otus.akn.project.soap.salary.client package. 
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

    private final static QName _GetMaxEmployeesSalaryResponse_QNAME = new QName("http://salary.soap.project.akn.otus.ru/", "getMaxEmployeesSalaryResponse");
    private final static QName _CalculateAverageSalaryResponse_QNAME = new QName("http://salary.soap.project.akn.otus.ru/", "calculateAverageSalaryResponse");
    private final static QName _CalculateAverageSalary_QNAME = new QName("http://salary.soap.project.akn.otus.ru/", "calculateAverageSalary");
    private final static QName _GetMaxEmployeesSalary_QNAME = new QName("http://salary.soap.project.akn.otus.ru/", "getMaxEmployeesSalary");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ru.otus.akn.project.soap.salary.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetMaxEmployeesSalaryResponse }
     * 
     */
    public GetMaxEmployeesSalaryResponse createGetMaxEmployeesSalaryResponse() {
        return new GetMaxEmployeesSalaryResponse();
    }

    /**
     * Create an instance of {@link CalculateAverageSalary }
     * 
     */
    public CalculateAverageSalary createCalculateAverageSalary() {
        return new CalculateAverageSalary();
    }

    /**
     * Create an instance of {@link GetMaxEmployeesSalary }
     * 
     */
    public GetMaxEmployeesSalary createGetMaxEmployeesSalary() {
        return new GetMaxEmployeesSalary();
    }

    /**
     * Create an instance of {@link CalculateAverageSalaryResponse }
     * 
     */
    public CalculateAverageSalaryResponse createCalculateAverageSalaryResponse() {
        return new CalculateAverageSalaryResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMaxEmployeesSalaryResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://salary.soap.project.akn.otus.ru/", name = "getMaxEmployeesSalaryResponse")
    public JAXBElement<GetMaxEmployeesSalaryResponse> createGetMaxEmployeesSalaryResponse(GetMaxEmployeesSalaryResponse value) {
        return new JAXBElement<GetMaxEmployeesSalaryResponse>(_GetMaxEmployeesSalaryResponse_QNAME, GetMaxEmployeesSalaryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CalculateAverageSalaryResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://salary.soap.project.akn.otus.ru/", name = "calculateAverageSalaryResponse")
    public JAXBElement<CalculateAverageSalaryResponse> createCalculateAverageSalaryResponse(CalculateAverageSalaryResponse value) {
        return new JAXBElement<CalculateAverageSalaryResponse>(_CalculateAverageSalaryResponse_QNAME, CalculateAverageSalaryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CalculateAverageSalary }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://salary.soap.project.akn.otus.ru/", name = "calculateAverageSalary")
    public JAXBElement<CalculateAverageSalary> createCalculateAverageSalary(CalculateAverageSalary value) {
        return new JAXBElement<CalculateAverageSalary>(_CalculateAverageSalary_QNAME, CalculateAverageSalary.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMaxEmployeesSalary }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://salary.soap.project.akn.otus.ru/", name = "getMaxEmployeesSalary")
    public JAXBElement<GetMaxEmployeesSalary> createGetMaxEmployeesSalary(GetMaxEmployeesSalary value) {
        return new JAXBElement<GetMaxEmployeesSalary>(_GetMaxEmployeesSalary_QNAME, GetMaxEmployeesSalary.class, null, value);
    }

}
