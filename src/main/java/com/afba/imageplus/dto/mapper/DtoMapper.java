package com.afba.imageplus.dto.mapper;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper<FROM, TO> {

    public TO convert(FROM dto, Class<TO> to) {
        var entity = BeanUtils.instantiateClass(to);
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
