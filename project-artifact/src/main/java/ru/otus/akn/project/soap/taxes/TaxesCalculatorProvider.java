package ru.otus.akn.project.soap.taxes;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import java.math.BigDecimal;

public interface TaxesCalculatorProvider {

    @WebMethod
    @WebResult(name = "calculationResult")
    BigDecimal calculateTaxes(@WebParam BigDecimal income,
                              @WebParam BigDecimal expenses,
                              @WebParam BigDecimal taxRate);

}
