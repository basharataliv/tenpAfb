package com.afba.imageplus.model.sqlserver.converter;

import com.afba.imageplus.model.sqlserver.ICRBuffer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ICRBufferConverter implements AttributeConverter<ICRBuffer, String> {

    private final ObjectMapper mapper;

    public ICRBufferConverter() {
        mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.findAndRegisterModules();
    }

    @SneakyThrows
    @Override
    public String convertToDatabaseColumn(ICRBuffer bufferObject) {
        if (bufferObject == null) {
            return null;
        }
        return mapper.writeValueAsString(bufferObject);
    }

    @Override
    public ICRBuffer convertToEntityAttribute(String bufferJson) {
        if (StringUtils.isEmpty(bufferJson)) {
            return null;
        }
        try {
            return mapper.readValue(bufferJson, ICRBuffer.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }


}
