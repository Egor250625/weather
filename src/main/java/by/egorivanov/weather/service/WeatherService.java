package by.egorivanov.weather.service;

import by.egorivanov.weather.config.OpenWeatherConfig;
import by.egorivanov.weather.dto.response.WeatherByCoordinateResponseDto;
import by.egorivanov.weather.dto.response.WeatherByNameResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class WeatherService {
    public final RestTemplate restTemplate;
    public final OpenWeatherConfig config;

    public WeatherByCoordinateResponseDto getWeatherByCoordinates(double lat, double lon) {
        if (lat < -90 || lat > 90 || lon < -180 || lon > 180) {
            throw new IllegalArgumentException("Wrong coordinates");
        }

        String url = String.format("%s/data/2.5/weather?lat=%s&lon=%s&appid=%s&units=metric",
                config.getUrl(), lat, lon, config.getKey());

        try {
            log.info("Weather request in process...");
            WeatherByCoordinateResponseDto response = restTemplate.getForObject(url,
                    WeatherByCoordinateResponseDto.class);
            if (response == null) {
                log.warn("Answer from OpenWeather is empty for coordinates lat={}, lon={}", lat, lon);
                throw new RuntimeException("Weather data error");
            }
            log.info("Successful answer from OpenWeather  for {}, {}", lat, lon);
            return response;
        } catch (RestClientException e) {
            log.error("Error request OpenWeather API: {}", e.getMessage(), e);
            throw new RuntimeException("Error receiving data from OpenWeather API.");
        }

    }


    public List<WeatherByNameResponseDto> getWeatherByCityName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("City name must not be empty");
        }

        String encodedName = URLEncoder.encode(name.trim(), StandardCharsets.UTF_8);
        String url = String.format("%s/geo/1.0/direct?q=%s&limit=5&appid=%s",
                config.getUrl(), encodedName, config.getKey());

        try {
            log.info("Sending request to OpenWeather geo API for name: {}", name);
            WeatherByNameResponseDto[] response = restTemplate.getForObject(url, WeatherByNameResponseDto[].class);

            if (response == null || response.length == 0) {
                log.warn("No results found for: {}", name);
                return Collections.emptyList();
            }

            log.info("Found {} result(s) for: {}", response.length, name);
            System.out.println("Results from OpenWeather API:");
            return Arrays.asList(response);
        } catch (RestClientException e) {
            log.error("Error fetching data from OpenWeather for name '{}': {}", name, e.getMessage(), e);
            throw new RuntimeException("OpenWeather API request failed.");
        }
    }


}
