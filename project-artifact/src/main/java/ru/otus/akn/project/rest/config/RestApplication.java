package ru.otus.akn.project.rest.config;

import ru.otus.akn.project.rest.exception.RuntimeExceptionMapper;
import ru.otus.akn.project.rest.filter.CorsFilter;
import ru.otus.akn.project.rest.payments.AnnuityPayment;
import ru.otus.akn.project.rest.payments.DifferentiatedPayment;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;
import static ru.otus.akn.project.rest.config.RestApplication.REST_URL;

@ApplicationPath(REST_URL)
public class RestApplication extends Application {

    static final String REST_URL = "/api";
    private static final String ENCODING_PROPERTY = "encoding";
    private Set<Class<?>> classes = new HashSet<>();

    public RestApplication() {
        registerResources();
        registerExceptionMappers();
        registerFilters();
    }

    private void registerFilters() {
        register(CorsFilter.class);
    }

    private void registerExceptionMappers() {
        register(RuntimeExceptionMapper.class);
    }

    private void registerResources() {
        register(AnnuityPayment.class);
        register(DifferentiatedPayment.class);
    }

    public void register(Class clazz) {
        classes.add(clazz);
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }

    @Override
    public Map<String, Object> getProperties() {
        return new HashMap<String, Object>() {{
            put(ENCODING_PROPERTY, UTF_8.toString());
        }};
    }
}
