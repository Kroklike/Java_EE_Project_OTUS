package ru.otus.akn.project.ejb.services.stateless;

import ru.otus.akn.project.ejb.api.stateless.PaymentCalculatorService;

import javax.ejb.Stateless;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Stateless
public class PaymentCalculatorServiceImpl implements PaymentCalculatorService {

    private static final BigDecimal MONTHS_IN_A_YEAR = BigDecimal.valueOf(12);
    private static final BigDecimal TO_PERCENTS = BigDecimal.valueOf(100);
    private static final String VALUES_CHECK_FAILED_MESSAGE =
            "Number of periods, Credit amount and Interest rate should be positive values.";

    @Override
    public BigDecimal calculateAnnuityPayments(int numberOfPeriods,
                                               @NotNull BigDecimal creditAmount,
                                               @NotNull BigDecimal interestRateByYear) {

        checkInputValues(numberOfPeriods, creditAmount, interestRateByYear);

        MathContext calculatingContext = new MathContext(8, RoundingMode.HALF_UP);
        BigDecimal interestRateValue = interestRateByYear
                .divide(TO_PERCENTS, calculatingContext)
                .divide(MONTHS_IN_A_YEAR, calculatingContext);
        BigDecimal result = BigDecimal.valueOf(1)
                .add(interestRateValue, calculatingContext)
                .pow(numberOfPeriods, calculatingContext);
        result = BigDecimal.valueOf(1).divide(result, calculatingContext);
        result = BigDecimal.valueOf(1).subtract(result, calculatingContext);
        return creditAmount.multiply(interestRateValue, calculatingContext)
                .divide(result, calculatingContext).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal[] calculateDifferentiatedPayments(int numberOfPeriods,
                                                        @NotNull BigDecimal creditAmount,
                                                        @NotNull BigDecimal interestRateByYear) {

        checkInputValues(numberOfPeriods, creditAmount, interestRateByYear);

        BigDecimal[] result = new BigDecimal[numberOfPeriods];
        MathContext calculatingContext = new MathContext(8, RoundingMode.HALF_UP);
        BigDecimal startValue = creditAmount.divide(BigDecimal.valueOf(numberOfPeriods), calculatingContext);
        BigDecimal interestRateValue = interestRateByYear
                .divide(TO_PERCENTS, calculatingContext)
                .divide(MONTHS_IN_A_YEAR, calculatingContext)
                .divide(BigDecimal.valueOf(numberOfPeriods), calculatingContext);

        for (int i = 1; i <= numberOfPeriods; i++) {
            int multiplyValue = numberOfPeriods - i + 1;
            result[i - 1] = interestRateValue.multiply(BigDecimal.valueOf(multiplyValue), calculatingContext)
                    .multiply(creditAmount, calculatingContext).add(startValue, calculatingContext)
                    .setScale(2, RoundingMode.HALF_UP);
        }

        return result;
    }

    private void checkInputValues(int numberOfPeriods, @NotNull BigDecimal creditAmount, @NotNull BigDecimal interestRateByYear) {
        if (numberOfPeriods <= 0 ||
                creditAmount.compareTo(BigDecimal.ZERO) <= 0 ||
                interestRateByYear.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(VALUES_CHECK_FAILED_MESSAGE);
        }
    }
}
