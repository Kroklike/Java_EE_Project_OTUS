package ru.otus.akn.project.rest.payments;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

@Path("/calculator/v2")
public class AnnuityPayment implements PaymentCalculator {

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

        MathContext calculatingContext = new MathContext(8, RoundingMode.HALF_UP);
        BigDecimal interestRateValue = interestRateByYear
                .divide(TO_PERCENTS, calculatingContext)
                .divide(MONTHS_IN_A_YEAR, calculatingContext);
        BigDecimal result = BigDecimal.valueOf(1)
                .add(interestRateValue, calculatingContext)
                .pow(numberOfPeriods, calculatingContext);
        result = BigDecimal.valueOf(1).divide(result, calculatingContext);
        result = BigDecimal.valueOf(1).subtract(result, calculatingContext);
        result = creditAmount.multiply(interestRateValue, calculatingContext)
                .divide(result, calculatingContext).setScale(2, RoundingMode.HALF_UP);

        return Response.status(200).entity(result).build();
    }
}
