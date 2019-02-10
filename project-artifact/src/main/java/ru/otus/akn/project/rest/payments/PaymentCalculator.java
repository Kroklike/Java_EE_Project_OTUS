package ru.otus.akn.project.rest.payments;

import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

public interface PaymentCalculator {

    Response calculatePayments(int numberOfPeriods,
                               @NotNull BigDecimal creditAmount,
                               @NotNull BigDecimal interestRateByYear);

}
