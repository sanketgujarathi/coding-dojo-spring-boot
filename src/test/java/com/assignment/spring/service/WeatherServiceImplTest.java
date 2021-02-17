package com.assignment.spring.service;

import com.assignment.spring.config.AppConfig;
import com.assignment.spring.entity.Weather;
import com.assignment.spring.exception.WeatherApiException;
import com.assignment.spring.repository.WeatherRepository;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AppConfig.class, initializers = ConfigFileApplicationContextInitializer.class)
@AutoConfigureWebClient
public class WeatherServiceImplTest {

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @MockBean
    private WeatherRepository weatherRepository;

    private WeatherService weatherService;

    @Value("${weather.api.url}")
    private String weatherApiUrl;

    @Value("${weather.api.app-id}")
    private String weatherAppId;

    private static final String API_RESPONSE = "{\"coord\":{\"lon\":4.8897,\"lat\":52.374},\"weather\":[{\"id\":701,\"main\":\"Mist\",\"description\":\"mist\",\"icon\":\"50n\"}],\"base\":\"stations\",\"main\":{\"temp\":277.81,\"feels_like\":273.02,\"temp_min\":277.04,\"temp_max\":278.71,\"pressure\":1020,\"humidity\":100},\"visibility\":2500,\"wind\":{\"speed\":5.14,\"deg\":210},\"clouds\":{\"all\":75},\"dt\":1613412051,\"sys\":{\"type\":1,\"id\":1524,\"country\":\"NL\",\"sunrise\":1613372182,\"sunset\":1613407981},\"timezone\":3600,\"id\":2759794,\"name\":\"Amsterdam\",\"cod\":200}";
    private static final String QUERY = "?q=Amsterdam&APPID=dummy";
    private static final String AMSTERDAM = "Amsterdam";


    @Before
    public void init(){
        mockServer = MockRestServiceServer.createServer(restTemplate);
        MockitoAnnotations.initMocks(this);
        weatherService = new WeatherServiceImpl(restTemplate, weatherRepository, weatherApiUrl, weatherAppId);
    }

    @Test
    public void getWeather_HappyPath() {
        mockServer.expect(ExpectedCount.once(), requestTo(weatherApiUrl.concat(QUERY)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess().contentType(MediaType.APPLICATION_JSON).body(API_RESPONSE));
        Mockito.when(weatherRepository.save(any(Weather.class))).then(AdditionalAnswers.returnsFirstArg());

        Optional<Weather> weather = weatherService.getWeather(AMSTERDAM);

        mockServer.verify();
        verify(weatherRepository, times(1)).save(any(Weather.class));
        Assert.assertTrue("Weather object should be present", weather.isPresent());

    }

    @Test
    public void getWeather_NotFound_Return_Empty() {
        mockServer.expect(ExpectedCount.once(), requestTo(weatherApiUrl.concat(QUERY)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));
        Mockito.when(weatherRepository.save(any(Weather.class))).then(AdditionalAnswers.returnsFirstArg());

        Optional<Weather> weather = weatherService.getWeather(AMSTERDAM);

        mockServer.verify();
        verify(weatherRepository, never()).save(any(Weather.class));
        Assert.assertFalse("Weather object should not be present", weather.isPresent());

    }

    @Test
    public void getWeather_UnauthorisedRequest() {
        mockServer.expect(ExpectedCount.once(), requestTo(weatherApiUrl.concat(QUERY)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withUnauthorizedRequest());

        Assertions.assertThatThrownBy(() -> weatherService.getWeather(AMSTERDAM))
                .isInstanceOf(WeatherApiException.class);
        mockServer.verify();
        verify(weatherRepository, never()).save(any(Weather.class));

    }

    @Test
    public void getWeather_BadRequest() {
        mockServer.expect(ExpectedCount.once(), requestTo(weatherApiUrl.concat(QUERY)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withBadRequest());

        Assertions.assertThatThrownBy(() -> weatherService.getWeather(AMSTERDAM))
                .isInstanceOf(WeatherApiException.class);
        mockServer.verify();
        verify(weatherRepository, never()).save(any(Weather.class));

    }

    @Test
    public void getWeather_ServerError() {
        mockServer.expect(ExpectedCount.once(), requestTo(weatherApiUrl.concat(QUERY)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withServerError());

        Assertions.assertThatThrownBy(() -> weatherService.getWeather(AMSTERDAM))
                .isInstanceOf(WeatherApiException.class);
        mockServer.verify();
        verify(weatherRepository, never()).save(any(Weather.class));

    }
}