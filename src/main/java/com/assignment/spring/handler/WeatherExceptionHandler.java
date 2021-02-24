package com.assignment.spring.handler;

import com.assignment.spring.exception.WeatherApiException;
import com.assignment.spring.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class WeatherExceptionHandler {

    private static Logger log = LoggerFactory.getLogger(WeatherExceptionHandler.class);

    @ExceptionHandler({WeatherApiException.class, DataAccessException.class})
    public ResponseEntity<ErrorResponse> handleWeatherApiExceptions(Exception e){
        String msg = "Error occurred while processing request";
        log.error(msg, e);
        ErrorResponse error = new ErrorResponse();
        error.setMessage(msg);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);

    }

    @ExceptionHandler({MethodArgumentNotValidException.class, MissingServletRequestParameterException.class, ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleValidationExceptions(Exception e){

        String msg = "Invalid value passed in request";
        log.error(msg, e);
        ErrorResponse error = new ErrorResponse();
        error.setMessage(msg);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

    }
}


