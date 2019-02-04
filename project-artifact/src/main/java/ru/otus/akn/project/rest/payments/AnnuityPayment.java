package ru.otus.akn.project.rest.payments;

import io.swagger.annotations.*;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

@Path("/calculator/v2")
@Api(tags = "Calculator version 2")
@SwaggerDefinition(
        info = @Info(
                title = "Swagger-generated RESTful API",
                description = "RESTful Description",
                version = "1.0.0",
                termsOfService = "Free to use",
                contact = @Contact(
                        name = "Kirill Art", email = "IAMKIRART@gmail.com",
                        url = "https://google.com"),
                license = @License(
                        name = "Apache 2.0",
                        url = "http://www.apache.org")),
        tags = {
                @Tag(name = "Calculator version 2", description = "RESTful API to calculate annuity payment")
        },
        host = "localhost:8080",
        basePath = "/api",
        schemes = {SwaggerDefinition.Scheme.HTTP},
        externalDocs = @ExternalDocs(
                value = "Swagger REST",
                url = "https://tinyurl.com/swagger-wlp"))
public class AnnuityPayment implements PaymentCalculator {

    @GET
    @Path("/calculate")
    @ApiOperation("Calculate annuity payment")
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
