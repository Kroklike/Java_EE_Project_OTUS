package ru.otus.akn.project.rest.payments;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

@Path("/calculator/v1")
public class DifferentiatedPayment implements PaymentCalculator {

    @GET
    @Path("/calculate")
    @Produces(APPLICATION_JSON)
    @Override
    public Response calculatePayments(@QueryParam("numberOfPeriods") int numberOfPeriods,
                                      @QueryParam("creditAmount") @NotNull BigDecimal creditAmount,
                                      @QueryParam("interestRateByYear") @NotNull BigDecimal interestRateByYear) {

        if (numberOfPeriods <= 0 ||
                creditAmount.compareTo(BigDecimal.ZERO) <= 0 ||
                interestRateByYear.compareTo(BigDecimal.ZERO) <= 0) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
                    .entity("Number of periods, Credit amount and Interest rate should be positive values.")
                    .type(TEXT_PLAIN)
                    .build());
        }

        String[] output = new String[numberOfPeriods];
        MathContext calculatingContext = new MathContext(8, RoundingMode.HALF_UP);
        BigDecimal startValue = creditAmount.divide(BigDecimal.valueOf(numberOfPeriods), calculatingContext);
        BigDecimal interestRateValue = interestRateByYear
                .divide(TO_PERCENTS, calculatingContext)
                .divide(MONTHS_IN_A_YEAR, calculatingContext)
                .divide(BigDecimal.valueOf(numberOfPeriods), calculatingContext);

        for (int i = 1; i <= numberOfPeriods; i++) {
            int multiplyValue = numberOfPeriods - i + 1;
            BigDecimal result = interestRateValue.multiply(BigDecimal.valueOf(multiplyValue), calculatingContext)
                    .multiply(creditAmount, calculatingContext).add(startValue, calculatingContext);

            output[i - 1] = i + " месяц: " + result.setScale(2, RoundingMode.HALF_UP);
        }
        return Response.status(200).entity(output).build();
    }
}
