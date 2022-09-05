package com.afba.imageplus.controller;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.dto.mapper.BaseMapper;
import com.afba.imageplus.dto.res.BaseResponseDto;
import com.afba.imageplus.dto.validation.group.Insert;
import com.afba.imageplus.dto.validation.group.Update;
import com.afba.imageplus.model.sqlserver.BaseEntity;
import com.afba.imageplus.service.BaseService;
import com.afba.imageplus.service.ErrorService;
import com.afba.imageplus.utilities.PropertyUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.IdClass;
import java.beans.PropertyDescriptor;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;

public abstract class BaseController<E extends BaseEntity, ID, DtoReq, DtoRes> {

    private Class<E> entityClass;
    private Class<ID> idClass;
    private Class<DtoReq> dtoReqClass;
    private Class<DtoRes> dtoResClass;
    private ConversionService conversionService;

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    protected ErrorService errorService;

    protected BaseService<E, ID> service;
    protected BaseMapper<E, DtoReq> requestMapper;
    protected BaseMapper<E, DtoRes> responseMapper;

    protected BaseController(BaseService<E, ID> service, BaseMapper<E, DtoReq> requestMapper,
            BaseMapper<E, DtoRes> responseMapper) {
        this.service = service;
        this.requestMapper = requestMapper;
        this.responseMapper = responseMapper;
    }

    @ApiOperation("Returns single record against the given ID")
    @GetMapping("{id}")
    public BaseResponseDto<DtoRes> findById(@PathVariable Map<String, String> pathVariables) {
        var optionalEntity = service.findById(getIdFromPath(pathVariables));
        if (optionalEntity.isEmpty()) {
            return errorService.throwException(HttpStatus.NOT_FOUND,
                    service.getEKDError(String.valueOf(HttpStatus.NOT_FOUND.value())), pathVariables.get("id"));
        }
        return BaseResponseDto.success(responseMapper.convert(optionalEntity.get(), getDtoResClass()));
    }

    @ApiOperation("Returns a paginated list of records against given filter criteria")
    @GetMapping
    public BaseResponseDto<Page<DtoRes>> findAll(Pageable pageable, @RequestParam Map<String, Object> filters,
            @PathVariable Map<String, String> pathVariables) {
        filters.putAll(pathVariables);
        return BaseResponseDto
                .success(service.findAll(pageable, filters).map(e -> responseMapper.convert(e, getDtoResClass())));
    }

    @ApiOperation("Inserts a new record in the table with given values. If the ID is not auto generated and given ID already exists in the table, the operation will fail with CONFLICT status.")
    @PostMapping
    public BaseResponseDto<DtoRes> insert(@PathVariable Map<String, String> pathVariables,
            @Validated(Insert.class) @RequestBody DtoReq reqDto) {
        var entity = requestMapper.convert(reqDto, getEntityClass());
        mapPathVariables(pathVariables, entity);
        return BaseResponseDto.success(responseMapper.convert(service.insert(entity), getDtoResClass()));
    }

    @ApiOperation("Updates an existing record in the table with given values. If the record is not found against given ID the request will fail with NOT_FOUND status.")
    @PutMapping("{id}")
    public BaseResponseDto<DtoRes> update(@PathVariable Map<String, String> pathVariables,
            @Validated(Update.class) @RequestBody DtoReq reqDto) {
        var entity = requestMapper.convert(reqDto, getEntityClass());
        mapPathVariables(pathVariables, entity);
        return BaseResponseDto.success(
                responseMapper.convert(service.update(getIdFromPath(pathVariables), entity), getDtoResClass()));
    }

    @ApiOperation("Deletes a record from table against given ID. If the record is not found against given ID the request will fail with NOT_FOUND status.")
    @DeleteMapping("{id}")
    public BaseResponseDto<String> delete(@PathVariable Map<String, String> pathVariables) {
        var id = getIdFromPath(pathVariables);
        service.delete(id);
        return BaseResponseDto
                .success(String.format("Record with ID: %s is deleted from %s", id, getEntityClass().getSimpleName()));
    }

    protected Class<E> getEntityClass() {
        if (entityClass == null) {
            entityClass = (Class<E>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                    .getActualTypeArguments()[0];
        }
        return entityClass;
    }

    protected Class<ID> getIdClass() {
        if (idClass == null) {
            idClass = (Class<ID>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                    .getActualTypeArguments()[1];
        }
        return idClass;
    }

    protected Class<DtoReq> getDtoReqClass() {
        if (dtoReqClass == null) {
            dtoReqClass = (Class<DtoReq>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                    .getActualTypeArguments()[2];
        }
        return dtoReqClass;
    }

    protected Class<DtoRes> getDtoResClass() {
        if (dtoResClass == null) {
            dtoResClass = (Class<DtoRes>) ((ParameterizedType) this.getClass().getGenericSuperclass())
                    .getActualTypeArguments()[3];
        }
        return dtoResClass;
    }

    protected ConversionService getConversionService() {
        if (conversionService == null) {
            conversionService = (ConversionService) applicationContext.getBean("mvcConversionService");
        }
        return conversionService;
    }

    protected ID getIdFromPath(final Map<String, String> pathVariables) {
        if (!getEntityClass().isAnnotationPresent(IdClass.class)) {
            return getConversionService().convert(pathVariables.get("id"), getIdClass());
        }
        var pathVariablesCI = new LinkedCaseInsensitiveMap<>();
        pathVariablesCI.putAll(pathVariables);
        var id = BeanUtils.instantiateClass(getIdClass());
        var unResolvedKeys = new LinkedList<PropertyDescriptor>();
        for (var idProperty : BeanUtils.getPropertyDescriptors(getIdClass())) {
            if (pathVariablesCI.containsKey(idProperty.getName())) {
                PropertyUtil.setPropertyValue(idProperty, id, pathVariablesCI.get(idProperty.getName()));
            } else {
                unResolvedKeys.add(idProperty);
            }
        }
        if (unResolvedKeys.size() != 1) {
            throw new DomainException(HttpStatus.INTERNAL_SERVER_ERROR, EKDError.EKD000500.code(),
                    "Id properties of entity and path variables does not match.");
        }
        PropertyUtil.setPropertyValue(unResolvedKeys.getLast(), id, pathVariablesCI.get("id"));
        return id;
    }

    protected void mapPathVariables(Map<String, String> pathVariables, Object object) {
        for (var variable : pathVariables.entrySet()) {
            Arrays.stream(BeanUtils.getPropertyDescriptors(object.getClass())).forEach(propertyDescriptor -> {
                if (propertyDescriptor.getName().equals(variable.getKey())) {
                    var value = getConversionService().convert(variable.getValue(),
                            propertyDescriptor.getPropertyType());
                    PropertyUtil.setPropertyValue(propertyDescriptor, object, value);
                }
            });
        }
    }

}
