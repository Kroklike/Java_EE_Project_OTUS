package ru.otus.akn.project.rest.payments;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import ru.otus.akn.project.ejb.api.stateless.PaymentCalculatorService;

import javax.ejb.EJB;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

@Path("/calculator/v1")
@Api(tags = "Calculator version 1")
@SwaggerDefinition(tags = {
        @Tag(name = "Calculator version 1", description = "RESTful API to calculate differentiated payment")
})
public class DifferentiatedPayment implements PaymentCalculator {

    @EJB
    private PaymentCalculatorService paymentCalculatorService;

    @GET
    @Path("/calculate")
    @ApiOperation("Calculate differentiated payment")
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
        BigDecimal[] result = paymentCalculatorService
                .calculateDifferentiatedPayments(numberOfPeriods, creditAmount, interestRateByYear);

        for (int i = 1; i <= numberOfPeriods; i++) {
            output[i - 1] = i + " месяц: " + result[i - 1];
        }
        return Response.status(200).entity(output).build();
    }
}
