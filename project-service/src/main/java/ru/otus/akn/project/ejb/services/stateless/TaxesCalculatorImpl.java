package ru.otus.akn.project.ejb.services.stateless;

import ru.otus.akn.project.ejb.api.stateless.TaxesCalculator;

import javax.ejb.Stateless;
import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;

@Stateless
public class TaxesCalculatorImpl implements TaxesCalculator {

    private static final String CHECK_INCOME_MESSAGE = "Приход должен быть указан и не быть отрицательным!";
    private static final String CHECK_EXPENSE_MESSAGE = "Расход должен быть указан и не быть отрицательным!";
    private static final String CHECK_TAX_RATE_MESSAGE = "Налоговая ставка должна быть указана и не быть отрицательна!";
    private static final BigDecimal MAX_TAX_RATE = BigDecimal.valueOf(100);
    private static final BigDecimal GET_PERCENT = BigDecimal.valueOf(100);

    @Override
    public BigDecimal calculateTaxes(BigDecimal income,
                                     BigDecimal expenses,
                                     BigDecimal taxRate) {
        checkParam(income, CHECK_INCOME_MESSAGE);
        checkParam(expenses, CHECK_EXPENSE_MESSAGE);
        checkParam(taxRate, CHECK_TAX_RATE_MESSAGE);

        if (taxRate.compareTo(MAX_TAX_RATE) > 0) {
            throw new RuntimeException("Налоговая ставка не должна быть больше " + MAX_TAX_RATE + "%!");
        }

        BigDecimal profit = income.subtract(expenses);

        //При убытках или нулевой прибыли возвращаем нуль
        if (profit.compareTo(ZERO) <= 0) {
            return ZERO;
        }

        BigDecimal taxBase = taxRate.divide(GET_PERCENT, 2, HALF_UP);

        return profit.multiply(taxBase).setScale(2, HALF_UP);
    }

    private void checkParam(BigDecimal income, String s) {
        if (income == null || income.compareTo(ZERO) < 0) {
            throw new RuntimeException(s);
        }
    }
}
