package com.backendparkingflypass.general.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ValidationException extends Exception {
    private static final Logger logger = LogManager.getLogger(ValidationException.class);

    private final HttpStatus status;
    private String msg;

    @Autowired
    private MessageSource messageSource;

    public ValidationException() {
        this.status = HttpStatus.BAD_REQUEST;
    }

    public ValidationException(String msg, HttpStatus status) {
        this.msg = msg;
        this.status = status;
        logger.error(msg);
    }

    public ValidationException(String message) {
        this.msg = message;
        this.status = HttpStatus.BAD_REQUEST;
        logger.error(message);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> processValidationException(ValidationException validationException) {
        return new ResponseEntity<>(validationException.getExceptionMessage(), validationException.status);
    }

    private String getExceptionMessage() {
        return this.msg != null ? this.msg : messageSource.getMessage("validationError", null, LocaleContextHolder.getLocale());
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
