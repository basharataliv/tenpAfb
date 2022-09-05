package com.afba.imageplus.model.sqlserver.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Converter
public class Numeric8ByteToLocalTimeConverter implements AttributeConverter<LocalTime, Integer> {
    @Override
    public Integer convertToDatabaseColumn(LocalTime time) {
        if(time == null) {
            return 0;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HmmssSS");
        return Integer.parseInt(formatter.format(time));
    }

    @Override
    public LocalTime convertToEntityAttribute(Integer time) {
        if(time == 0) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HmmssSS");
        if(time.toString().length()<7){
            return LocalTime.parse( String.format("%07d", time), formatter);
        }
        return LocalTime.parse(time.toString(), formatter);
    }
}
