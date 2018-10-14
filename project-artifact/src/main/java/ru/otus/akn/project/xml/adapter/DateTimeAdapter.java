package ru.otus.akn.project.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateTimeAdapter extends XmlAdapter<String, LocalDate> {
    private final static String DATE_FORMAT = "dd-MM-yyyy";

    @Override
    public LocalDate unmarshal(String xmlFormat) {
        return LocalDate.from(DateTimeFormatter.ofPattern(DATE_FORMAT).parse(xmlFormat));
    }

    @Override
    public String marshal(LocalDate rawObject) {
        return DateTimeFormatter.ofPattern(DATE_FORMAT).format(rawObject);
    }
}
