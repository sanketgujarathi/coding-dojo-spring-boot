package com.assignment.spring.service;

import com.assignment.spring.entity.Weather;
import com.assignment.spring.repository.WeatherRepository;
import com.assignment.spring.response.api.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherServiceImpl implements WeatherService {

    private RestTemplate restTemplate;

    private WeatherRepository weatherRepository;

    private final String WEATHER_API_URL;

    private final String WEATHER_APP_ID;

    public WeatherServiceImpl(RestTemplate restTemplate, WeatherRepository weatherRepository, @Value("${weather.api.url}") String weatherApiUrl, @Value("${weather.api.app-id}")String weatherAppId) {
        this.restTemplate = restTemplate;
        this.weatherRepository = weatherRepository;
        WEATHER_API_URL = weatherApiUrl;
        WEATHER_APP_ID = weatherAppId;
    }

    @Override
    public Weather getWeather(String city) {
        String url = String.format(WEATHER_API_URL, city, WEATHER_APP_ID);
        ResponseEntity<WeatherResponse> response = restTemplate.getForEntity(url, WeatherResponse.class);
        Weather weather = mapper(response.getBody());
        return weatherRepository.save(weather);
    }

    private Weather mapper(WeatherResponse response) {
        Weather weather = new Weather();
        weather.setCity(response.getName());
        weather.setCountry(response.getSys().getCountry());
        weather.setTemperature(response.getMain().getTemp());
        return weather;
    }
}
