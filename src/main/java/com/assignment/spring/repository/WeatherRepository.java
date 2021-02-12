package com.assignment.spring.repository;

import com.assignment.spring.entity.Weather;
import org.springframework.data.repository.CrudRepository;

public interface WeatherRepository extends CrudRepository<Weather, Integer> {
}
