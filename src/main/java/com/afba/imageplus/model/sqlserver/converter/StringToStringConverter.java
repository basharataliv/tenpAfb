package com.afba.imageplus.model.sqlserver.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class StringToStringConverter implements AttributeConverter<String, String> {
    @Override
    public String convertToDatabaseColumn(String value) {
        return value == null ? null : value.trim();
    }

    @Override
    public String convertToEntityAttribute(String value) {
        return value == null ? null : value.trim();
    }
}
