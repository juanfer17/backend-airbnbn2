package com.backendparkingflypass.general.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NoDataFoundException extends Exception {
    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<String> processNoDataFoundException(NoDataFoundException e) {
        return new ResponseEntity<>(messageSource.getMessage("noData", null, LocaleContextHolder.getLocale()), HttpStatus.NOT_FOUND);
    }
}
