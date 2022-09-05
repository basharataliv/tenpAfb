package com.afba.imageplus.model.sqlserver.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Converter(autoApply = true)
public class NumericToLocalDateConverter implements AttributeConverter<LocalDate, Integer> {
    @Override
    public Integer convertToDatabaseColumn(LocalDate date) {
        if(date == null) {
            return 0;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return Integer.parseInt(formatter.format(date));
    }

    @Override
    public LocalDate convertToEntityAttribute(Integer date) {
        if(date == 0 || date == 99999999) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(date.toString(), formatter);
    }
}
