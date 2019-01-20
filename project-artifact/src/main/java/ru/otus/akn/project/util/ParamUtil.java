package ru.otus.akn.project.util;

public class ParamUtil {

    private static final int MINUTES_BETWEEN_CHECKS_DEFAULT = 1;

    public static Long getMinutesBetweenChecksForServlet() {
        Long fromSystemProperty = Long.getLong("minutes.between.checks");
        return fromSystemProperty == null ? MINUTES_BETWEEN_CHECKS_DEFAULT : fromSystemProperty;
    }

}
