package com.afba.imageplus.dto.mapper;

import com.afba.imageplus.model.sqlserver.BaseEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;


@Component
public class BaseMapper<E extends BaseEntity, DTO> {

    public E convert(DTO dto, Class<E> to) {
        var entity = BeanUtils.instantiateClass(to);
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    public DTO convert(E entity, Class<DTO> to) {
        var dto = BeanUtils.instantiateClass(to);
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
