package com.assignment.spring.handler;

import com.assignment.spring.exception.WeatherApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

public class WeatherRestTemplateErrorHandler extends DefaultResponseErrorHandler {


    @Override
    protected void handleError(ClientHttpResponse response, HttpStatus statusCode) throws IOException {

        if(!statusCode.equals(HttpStatus.NOT_FOUND)){
            throw new WeatherApiException("Unable to fetch weather information");
        }
    }
}
