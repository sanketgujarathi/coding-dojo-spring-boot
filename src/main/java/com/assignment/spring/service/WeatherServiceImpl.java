package com.assignment.spring.service;

import com.assignment.spring.entity.Weather;
import com.assignment.spring.repository.WeatherRepository;
import com.assignment.spring.response.api.WeatherResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Service
public class WeatherServiceImpl implements WeatherService {

    private RestTemplate restTemplate;

    private WeatherRepository weatherRepository;

    private final String WEATHER_API_URL;

    private final String WEATHER_APP_ID;

    private static Logger log = LoggerFactory.getLogger(WeatherServiceImpl.class);

    private static final String CITY = "q";

    private static final String APP_ID = "APPID";

    public WeatherServiceImpl(RestTemplate restTemplate, WeatherRepository weatherRepository, @Value("${weather.api.url}") String weatherApiUrl, @Value("${weather.api.app-id}")String weatherAppId) {
        this.restTemplate = restTemplate;
        this.weatherRepository = weatherRepository;
        WEATHER_API_URL = weatherApiUrl;
        WEATHER_APP_ID = weatherAppId;
    }

    @Override
    public Optional<Weather> getWeather(final String city) {
        URI uri = UriComponentsBuilder.fromUriString(WEATHER_API_URL)
                .queryParam(CITY, city)
                .queryParam(APP_ID, WEATHER_APP_ID)
                .build()
                .toUri();
        log.info("Making API call to fetch weather information for city: {}", city);
        ResponseEntity<WeatherResponse> response = restTemplate.getForEntity(uri, WeatherResponse.class);
        if(!response.getStatusCode().isError() && response.hasBody()){
            log.info("Received API response with weather information for city: {} and status: {}", city, response.getStatusCode());
            Weather weather = mapper(response.getBody());
            log.info("Saving weather information in db for city: {}", weather.getCity());
            return Optional.of(weatherRepository.save(weather));
        }else {
            log.info("Weather information not found for city: {}", city);
            return Optional.empty();
        }
    }

    private Weather mapper(final WeatherResponse response) {
        Weather weather = new Weather();
        weather.setCity(response.getName());
        weather.setCountry(response.getSystem().getCountry());
        weather.setTemperature(response.getMain().getTemperature());
        return weather;
    }
}
