package ru.otus.akn.project.ejb.api.stateless;

import javax.ejb.Remote;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Remote
public interface TaxesCalculator {

    BigDecimal calculateTaxes(@NotNull BigDecimal income,
                              @NotNull BigDecimal expenses,
                              @NotNull BigDecimal taxRate);

}
