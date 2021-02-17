package com.assignment.spring.controller;

import com.assignment.spring.entity.Weather;
import com.assignment.spring.response.WeatherResponse;
import com.assignment.spring.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@Validated
public class WeatherController {

    private static final String CITY_VALIDATION_REGEX = "^[a-zA-z]+([\\s.-]?[a-zA-z])+$";
    private WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather")
    public ResponseEntity<WeatherResponse> weather(@RequestParam @NotEmpty @Pattern(regexp = CITY_VALIDATION_REGEX) final String city) {
        Optional<Weather> weather   = weatherService.getWeather(city);
        return weather
                .map(v -> ok(mapEntityToResponse(v)))
                .orElse(notFound().build());
    }

    private WeatherResponse mapEntityToResponse(final Weather weather){

        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.setId(weather.getId());
        weatherResponse.setCity(weather.getCity());
        weatherResponse.setCountry(weather.getCountry());
        weatherResponse.setTemperature(weather.getTemperature());

        return weatherResponse;
    }
}
