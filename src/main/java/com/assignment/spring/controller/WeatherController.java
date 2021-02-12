package com.assignment.spring.controller;

import com.assignment.spring.entity.WeatherEntity;
import com.assignment.spring.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/weather")
    public WeatherEntity weather(@RequestParam String city) {
        WeatherEntity weather = weatherService.getWeather(city);
        return weather;
    }
}
