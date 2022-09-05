package com.afba.imageplus.controller.exceptions;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.constants.ErrorConstants;
import com.afba.imageplus.dto.res.BaseResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.Date;

//TODO: Decide status code for hardcoded values.
@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<BaseResponseDto<ErrorDTO>> handleDomainException(DomainException ex) {
        var errorDto = ErrorDTO.builder().timestamp(new Date()).message(ex.getMessage()).data(ex.getData()).build();
        log.error("Exception with status Code: " + ex.getStatusCode(), ex);
        return ResponseEntity.status(ex.getHttpStatus()).contentType(MediaType.APPLICATION_JSON).body(BaseResponseDto.failure(ex.getStatusCode(), errorDto));
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<BaseResponseDto<ErrorDTO>> sqlException(SQLException ex) {
        var errorDto = ErrorDTO.builder().timestamp(new Date()).message("SQl Exception for invalid key").data(null)
                .build();
        log.error("Sql Exception", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseDto.failure("SQL000000", errorDto));
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<BaseResponseDto<ErrorDTO>> emptyResultDataAccessException(EmptyResultDataAccessException ex) {
        log.error("Empty Result data Exception", ex);
        var errorDto = ErrorDTO.builder().timestamp(new Date()).message(ErrorConstants.RESOURCE_NOT_FOUND).data(null)
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseDto.failure("EKD000404", errorDto));
    }

    public ResponseEntity<BaseResponseDto<ErrorDTO>> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex) {
        var errorDto = ErrorDTO.builder().timestamp(new Date()).message(ex.getMessage()).data(null).build();
        log.error("Data Integrity Violation Exception", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseDto.failure("SQL000000", errorDto));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponseDto<ErrorDTO>> entityNotFoundException(ConstraintViolationException ex) {
        log.error("entity Not Found Exception", ex);
        var errorDto = ErrorDTO.builder().timestamp(new Date()).message(ex.getMessage()).data(null).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(BaseResponseDto.failure(EKDError.EKD000400.toString(), errorDto));
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        final StringBuilder message = new StringBuilder();
        ex.getFieldErrors().forEach(val -> message.append(val.getDefaultMessage()).append(", "));
        message.delete(message.lastIndexOf(", "), message.length());
        var errorDto = ErrorDTO.builder().timestamp(new Date()).message(message.toString()).data(null).build();
        log.error("Exception : {}", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseDto.failure("EKD000400", errorDto));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleBindException(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        var errorDto = ErrorDTO.builder().timestamp(new Date()).message(ex.getMessage()).data(null).build();
        log.error("Exception : {}", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseDto.failure("EKD000400", errorDto));
    }

}