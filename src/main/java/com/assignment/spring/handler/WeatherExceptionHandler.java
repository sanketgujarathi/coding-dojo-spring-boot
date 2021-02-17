package com.assignment.spring.handler;

import com.assignment.spring.exception.WeatherApiException;
import com.assignment.spring.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class WeatherExceptionHandler {

    @ExceptionHandler(WeatherApiException.class)
    public ResponseEntity<ErrorResponse> handleWeatherApiExceptions(WeatherApiException e){
        ErrorResponse error = new ErrorResponse();
        error.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);

    }

    @ExceptionHandler({MethodArgumentNotValidException.class, MissingServletRequestParameterException.class, ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleValidationExceptions(Exception e){

        ErrorResponse error = new ErrorResponse();
        error.setMessage("Invalid value passed in request");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

    }
}


