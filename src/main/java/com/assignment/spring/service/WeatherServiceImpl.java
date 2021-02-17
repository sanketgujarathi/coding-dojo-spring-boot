package com.assignment.spring.service;

import com.assignment.spring.entity.Weather;
import com.assignment.spring.repository.WeatherRepository;
import com.assignment.spring.response.api.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    public Optional<Weather> getWeather(final String city) {
        URI uri = UriComponentsBuilder.fromUriString(WEATHER_API_URL)
                .queryParam("q", city)
                .queryParam("APPID", WEATHER_APP_ID)
                .build()
                .toUri();
        ResponseEntity<WeatherResponse> response = restTemplate.getForEntity(uri, WeatherResponse.class);
        if(!response.getStatusCode().isError() && response.hasBody()){
            Weather weather = mapper(response.getBody());
            return Optional.of(weatherRepository.save(weather));
        }else {
            return Optional.empty();
        }
    }

    private Weather mapper(final WeatherResponse response) {
        Weather weather = new Weather();
        weather.setCity(response.getName());
        weather.setCountry(response.getSys().getCountry());
        weather.setTemperature(response.getMain().getTemp());
        return weather;
    }
}
