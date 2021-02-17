package com.assignment.spring.service;

import com.assignment.spring.entity.Weather;

import java.util.Optional;

public interface WeatherService {

    Optional<Weather> getWeather(String city);
}
