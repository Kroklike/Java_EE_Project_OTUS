package ru.otus.akn.project.ejb.api.stateless;

import javax.ejb.Remote;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Remote
public interface PaymentCalculatorService {

    BigDecimal calculateAnnuityPayments(int numberOfPeriods,
                                        @NotNull BigDecimal creditAmount,
                                        @NotNull BigDecimal interestRateByYear);

    BigDecimal[] calculateDifferentiatedPayments(int numberOfPeriods,
                                               @NotNull BigDecimal creditAmount,
                                               @NotNull BigDecimal interestRateByYear);

}
