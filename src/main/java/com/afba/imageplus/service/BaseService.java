package com.afba.imageplus.service;

import com.afba.imageplus.constants.EKDError;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Optional;

public interface BaseService<E, ID> {

    Optional<E> findById(ID id);

    Page<E> findAll(Pageable pageable, Map<String, Object> filters);

    E save(E entity);

    E insert(E entity);

    E update(ID id, E entity);

    void delete(ID id);

    boolean existsById(ID id);

    EKDError getEKDError(String suffix);

    E findByIdOrElseThrow(ID id);
}
