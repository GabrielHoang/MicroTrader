package com.hawwaru.microtrader.converter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static picocli.CommandLine.ITypeConverter;

public class LocalDateConverter implements ITypeConverter<LocalDate> {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public LocalDate convert(String value) throws ParseException {
        return LocalDate.parse(value, formatter);
    }
}
