package com.afba.imageplus.service;

import com.afba.imageplus.constants.IError;
import com.afba.imageplus.model.sqlserver.Error;
import org.springframework.http.HttpStatus;

public interface ErrorService {

    Error get(IError error);
    String getMessage(IError error);
    String getMessage(IError error, Object... args);
    <T> T throwException(HttpStatus httpStatus, IError error);
    <T> T throwException(HttpStatus httpStatus, IError error, Object... args);
}
