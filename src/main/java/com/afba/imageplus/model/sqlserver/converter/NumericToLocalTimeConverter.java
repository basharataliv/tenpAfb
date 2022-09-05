package com.afba.imageplus.model.sqlserver.converter;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Converter(autoApply = true)
@Slf4j
public class NumericToLocalTimeConverter implements AttributeConverter<LocalTime, Integer> {
    @Override
    public Integer convertToDatabaseColumn(LocalTime time) {
        if (time == null) {
            return 0;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("Hmmss");
        return Integer.parseInt(formatter.format(time));
    }

    @Override
    public LocalTime convertToEntityAttribute(Integer time) {
        if (time == 0) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("Hmmss");


        try {

            return LocalTime.parse(String.format("%06d", time), formatter);
        } catch (Exception e) {
            log.error("Numeric to LocalTime conversion failed for value {}", time, e);
            return null;
        }

    }
}
