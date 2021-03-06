package com.tinkoff.timetable.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import com.tinkoff.timetable.response.ErrorResponse;
import com.tinkoff.timetable.response.IResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
@Log4j2
public class ExceptionsHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class,
            ValueInstantiationException.class, InvalidFormatException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<IResponse> handleAndReturnBadRequest(Exception e) {
        log.error(e.getMessage(), e.getCause());
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ResponseEntity<IResponse> handleAndReturnNotfound(Exception e) {
        log.error(e.getMessage(), e.getCause());
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({NoAccessException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    private ResponseEntity<IResponse> handleAndReturnUnAuth(Exception e) {
        log.error(e.getMessage(), e.getCause());
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

}

