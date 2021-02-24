package com.assignment.spring.controller;

import com.assignment.spring.entity.Weather;
import com.assignment.spring.exception.WeatherApiException;
import com.assignment.spring.service.WeatherService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherController.class)
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class WeatherControllerTest {

    private static final String VALIDATION_ERROR = "{\"message\":\"Invalid value passed in request\"}";
    private static final String SERVER_ERROR = "{\"message\":\"Error occurred while processing request\"}";
    private static final String AUTH_VALUE = "Basic YWRtaW46cGFzc3dvcmQ=";
    private static final String AUTH_HEADER = "Authorization";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetWeather_HappyPath() throws Exception {
        when(weatherService.getWeather(any(String.class))).thenReturn(Optional.of(getWeather()));
        String weatherOutput = "{\"id\":123,\"city\":\"Amsterdam\",\"country\":\"Netherlands\",\"temperature\":20.5}";
        this.mockMvc.perform(get("/weather?city=Amsterdam")
                            .header(AUTH_HEADER, AUTH_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .json(weatherOutput));
    }

    @Test
    public void testGetWeather_AlphaNumericInput_BadRequest() throws Exception {
        this.mockMvc.perform(get("/weather?city=Am5terdam")
                            .header(AUTH_HEADER, AUTH_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .json(VALIDATION_ERROR));
    }

    @Test
    public void testGetWeather_SpecialCharactersInput_BadRequest() throws Exception {
        this.mockMvc.perform(get("/weather?city=Amsterd@m")
                            .header(AUTH_HEADER, AUTH_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .json(VALIDATION_ERROR));
    }

    @Test
    public void testGetWeather_EmptyInput_BadRequest() throws Exception {
        this.mockMvc.perform(get("/weather?city=  ")
                            .header(AUTH_HEADER, AUTH_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .json(VALIDATION_ERROR));
    }

    @Test
    public void testGetWeather_NullInput_BadRequest() throws Exception {
        this.mockMvc.perform(get("/weather")
                            .header(AUTH_HEADER, AUTH_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .json(VALIDATION_ERROR));
    }

    @Test
    public void testGetWeather_NotFound() throws Exception {

        when(weatherService.getWeather(any(String.class))).thenReturn(Optional.empty());
        this.mockMvc.perform(get("/weather?city=xyz")
                            .header(AUTH_HEADER, AUTH_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetWeather_ServerError() throws Exception {

        when(weatherService.getWeather(any(String.class))).thenThrow(new WeatherApiException("Server Error"));
            this.mockMvc.perform(get("/weather?city=Amsterdam")
                                .header(AUTH_HEADER, AUTH_VALUE))
                .andDo(print())
                .andExpect(status().isInternalServerError() )
                .andExpect(content().json(SERVER_ERROR));
    }

    @Test
    public void testGetWeather_DatabaseError() throws Exception {

        when(weatherService.getWeather(any(String.class))).thenThrow(new DataAccessException("Server Error"){});
        this.mockMvc.perform(get("/weather?city=Amsterdam")
                .header(AUTH_HEADER, AUTH_VALUE))
                .andDo(print())
                .andExpect(status().isInternalServerError() )
                .andExpect(content().json(SERVER_ERROR));
    }

    @Test
    public void testGetWeather_MissingAuthorisation() throws Exception {

        when(weatherService.getWeather(any(String.class))).thenThrow(new WeatherApiException("Server Error"));
        this.mockMvc.perform(get("/weather?city=Amsterdam"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetWeather_WrongCredentials() throws Exception {

        when(weatherService.getWeather(any(String.class))).thenThrow(new WeatherApiException("Server Error"));
        this.mockMvc.perform(get("/weather?city=Amsterdam")
                .header(AUTH_HEADER, "Basic YWRtaW46cGFzc3dvcmQx"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    private Weather getWeather() {
        Weather weather = new Weather();
        weather.setId(123);
        weather.setCity("Amsterdam");
        weather.setCountry("Netherlands");
        weather.setTemperature(20.5);
        return weather;
    }
}