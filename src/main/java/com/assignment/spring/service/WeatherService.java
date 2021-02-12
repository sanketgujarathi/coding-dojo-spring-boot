package com.assignment.spring.service;

import com.assignment.spring.entity.Weather;

public interface WeatherService {

    Weather getWeather(String city);
}
