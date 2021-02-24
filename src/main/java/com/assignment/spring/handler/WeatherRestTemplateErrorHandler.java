package com.assignment.spring.handler;

import com.assignment.spring.exception.WeatherApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

public class WeatherRestTemplateErrorHandler extends DefaultResponseErrorHandler {

    private static Logger log = LoggerFactory.getLogger(WeatherRestTemplateErrorHandler.class);

    @Override
    protected void handleError(ClientHttpResponse response, HttpStatus statusCode) throws IOException {
        log.error("Received error response from API with status: {} and body: {}", statusCode, response.getBody());
        if(!statusCode.equals(HttpStatus.NOT_FOUND)){
            throw new WeatherApiException("Unable to fetch weather information");
        }
    }
}
