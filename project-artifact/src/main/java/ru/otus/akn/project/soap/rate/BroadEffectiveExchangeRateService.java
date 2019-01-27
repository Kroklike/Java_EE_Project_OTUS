package ru.otus.akn.project.soap.rate;

import ru.otus.akn.project.soap.ServiceProviderWithDataSource;

import javax.activation.DataSource;
import javax.annotation.Resource;
import javax.xml.ws.*;

import static javax.xml.ws.Service.Mode.MESSAGE;
import static javax.xml.ws.http.HTTPBinding.HTTP_BINDING;

@WebServiceProvider
@ServiceMode(value = MESSAGE)
@BindingType(value = HTTP_BINDING)
public class BroadEffectiveExchangeRateService implements Provider<DataSource> {
    private static final String BROAD_EFFECTIVE_EXCHANGE_RATE_URL = "http://www.webservicex.net:500/Prices?ticker=NBRUBIS";
    private ServiceProviderWithDataSource provider;

    @Resource
    protected WebServiceContext webServiceContext;

    @Override
    public DataSource invoke(DataSource request) {
        if (provider == null) {
            provider = new ServiceProviderWithDataSource(BROAD_EFFECTIVE_EXCHANGE_RATE_URL, webServiceContext);
        }
        return provider.invoke(request);
    }
}
