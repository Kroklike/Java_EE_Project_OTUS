package ru.otus.akn.project.rest.payments;

import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

public interface PaymentCalculator {

    BigDecimal MONTHS_IN_A_YEAR = BigDecimal.valueOf(12);
    BigDecimal TO_PERCENTS = BigDecimal.valueOf(100);

    Response calculatePayments(int numberOfPeriods,
                               @NotNull BigDecimal creditAmount,
                               @NotNull BigDecimal interestRate);

}
