package com.afba.imageplus.service.impl;

import com.afba.imageplus.constants.IError;
import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.model.sqlserver.Error;
import com.afba.imageplus.repository.sqlserver.ErrorRepository;
import com.afba.imageplus.service.ErrorService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ErrorServiceImp implements ErrorService {

    private Map<String, Error> errors;
    private final ErrorRepository errorRepository;

    public ErrorServiceImp(ErrorRepository errorRepository) {
        this.errorRepository = errorRepository;
    }

    @PostConstruct
    public void loadErrors() {
        this.errors = this.errorRepository
                .findAll()
                .stream()
                .collect(Collectors.toMap(Error::getCode, error -> error));
    }

    @Override
    public Error get(IError error) {
        return this.errors.get(error.code());
    }

    @Override
    public String getMessage(IError error) {
        var err = this.errors.get(error.code());
        if(err == null) {
            return "";
        }
        return err.getMessage();
    }

    @Override
    public String getMessage(IError error, Object... args) {
        return String.format(getMessage(error), args);
    }

    @Override
    public <T> T throwException(HttpStatus httpStatus, IError error) {
        throw new DomainException(httpStatus, error.code(), getMessage(error));
    }

    @Override
    public <T> T throwException(HttpStatus httpStatus, IError error, Object... args) {
        throw new DomainException(httpStatus, error.code(), getMessage(error, args));
    }



}
