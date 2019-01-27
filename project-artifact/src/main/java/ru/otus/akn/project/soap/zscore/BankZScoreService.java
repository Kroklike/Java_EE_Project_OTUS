package ru.otus.akn.project.soap.zscore;

import ru.otus.akn.project.soap.ServiceProviderWithDataSource;

import javax.activation.DataSource;
import javax.annotation.Resource;
import javax.xml.ws.*;

import static javax.xml.ws.Service.Mode.MESSAGE;
import static javax.xml.ws.http.HTTPBinding.HTTP_BINDING;

@WebServiceProvider
@ServiceMode(value = MESSAGE)
@BindingType(value = HTTP_BINDING)
public class BankZScoreService implements Provider<DataSource> {
    private static final String BANK_Z_SCORE_URL = "http://www.webservicex.net:500/Prices?ticker=DDSI01RUA645NWDB";
    private ServiceProviderWithDataSource provider;

    @Resource
    protected WebServiceContext webServiceContext;

    @Override
    public DataSource invoke(DataSource request) {
        if (provider == null) {
            provider = new ServiceProviderWithDataSource(BANK_Z_SCORE_URL, webServiceContext);
        }
        return provider.invoke(request);
    }
}
