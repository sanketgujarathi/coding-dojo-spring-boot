package com.assignment.spring.service;

import com.assignment.spring.entity.Constants;
import com.assignment.spring.entity.Weather;
import com.assignment.spring.repository.WeatherRepository;
import com.assignment.spring.response.api.WeatherResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherServiceImpl implements WeatherService {

    private RestTemplate restTemplate;

    private WeatherRepository weatherRepository;

    public WeatherServiceImpl(RestTemplate restTemplate, WeatherRepository weatherRepository) {
        this.restTemplate = restTemplate;
        this.weatherRepository = weatherRepository;
    }

    @Override
    public Weather getWeather(String city) {
        String url = Constants.WEATHER_API_URL.replace("{city}", city).replace("{appid}", Constants.APP_ID);
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
