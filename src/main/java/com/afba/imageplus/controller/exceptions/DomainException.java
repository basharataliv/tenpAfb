package com.afba.imageplus.controller.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Data
public class DomainException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final HttpStatus httpStatus;
    private final String statusCode;
    private final Map<String, Object> data;

    /**
     * @deprecated Now the status code is mandatory.
     */
    @Deprecated(since = "0.4.0", forRemoval = true)
    public DomainException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.statusCode = null;
        this.data = null;
    }

    public DomainException(HttpStatus httpStatus, String statusCode, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.statusCode = statusCode;
        this.data = null;
    }

    public DomainException(HttpStatus httpStatus, String statusCode, String message, Map<String, Object> data) {
        super(message);
        this.statusCode = statusCode;
        this.httpStatus = httpStatus;
        this.data = data;
    }
}
