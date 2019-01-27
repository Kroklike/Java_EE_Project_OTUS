
package ru.otus.akn.project.soap.taxes.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;


/**
 * <p>Java class for calculateTaxes complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="calculateTaxes">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="arg0" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="arg1" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="arg2" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "calculateTaxes", propOrder = {
        "arg0",
        "arg1",
        "arg2"
})
public class CalculateTaxes {

    protected BigDecimal arg0;
    protected BigDecimal arg1;
    protected BigDecimal arg2;

    /**
     * Gets the value of the arg0 property.
     *
     * @return possible object is
     * {@link BigDecimal }
     */
    public BigDecimal getArg0() {
        return arg0;
    }

    /**
     * Sets the value of the arg0 property.
     *
     * @param value allowed object is
     *              {@link BigDecimal }
     */
    public void setArg0(BigDecimal value) {
        this.arg0 = value;
    }

    /**
     * Gets the value of the arg1 property.
     *
     * @return possible object is
     * {@link BigDecimal }
     */
    public BigDecimal getArg1() {
        return arg1;
    }

    /**
     * Sets the value of the arg1 property.
     *
     * @param value allowed object is
     *              {@link BigDecimal }
     */
    public void setArg1(BigDecimal value) {
        this.arg1 = value;
    }

    /**
     * Gets the value of the arg2 property.
     *
     * @return possible object is
     * {@link BigDecimal }
     */
    public BigDecimal getArg2() {
        return arg2;
    }

    /**
     * Sets the value of the arg2 property.
     *
     * @param value allowed object is
     *              {@link BigDecimal }
     */
    public void setArg2(BigDecimal value) {
        this.arg2 = value;
    }

}
