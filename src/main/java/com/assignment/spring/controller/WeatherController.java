package com.assignment.spring.controller;

import com.assignment.spring.entity.Weather;
import com.assignment.spring.response.WeatherResponse;
import com.assignment.spring.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

    private WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather")
    public ResponseEntity<WeatherResponse> weather(@RequestParam String city) {
        Weather weather = weatherService.getWeather(city);
        return ResponseEntity.ok(mapEntityToResponse(weather));
    }

    private WeatherResponse mapEntityToResponse(Weather weather){

        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.setId(weather.getId());
        weatherResponse.setCity(weather.getCity());
        weatherResponse.setCountry(weather.getCountry());
        weatherResponse.setTemperature(weather.getTemperature());

        return weatherResponse;
    }
}
