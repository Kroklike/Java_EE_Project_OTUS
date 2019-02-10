package ru.otus.akn.project.soap.taxes;

import ru.otus.akn.project.ejb.api.stateless.TaxesCalculator;

import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.math.BigDecimal;

@WebService(serviceName = "TaxesCalculatorService", name = "TaxesCalculatorProvider")
public class TaxesCalculatorService implements TaxesCalculatorProvider {

    @EJB
    private TaxesCalculator taxesCalculator;

    @WebMethod
    public BigDecimal calculateTaxes(@WebParam BigDecimal income,
                                     @WebParam BigDecimal expenses,
                                     @WebParam BigDecimal taxRate) {
        return taxesCalculator.calculateTaxes(income, expenses, taxRate);
    }
}
