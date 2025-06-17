package by.egorivanov.weather.service;

import by.egorivanov.weather.config.OpenWeatherConfig;
import by.egorivanov.weather.dto.response.WeatherByCoordinateResponseDto;
import by.egorivanov.weather.dto.response.WeatherByNameResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

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

        String url = String.format("%s/weather?lat=%s&lon=%s&appid=%s&units=metric",
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
        }catch (RestClientException e){
            log.error("Error request OpenWeather API: {}", e.getMessage(), e);
            throw new RuntimeException("Error receiving data from OpenWeather API.");
        }

    }


    public WeatherByNameResponseDto getWeatherByCityName(String cityName) {
        if (cityName == null || cityName.isBlank()) {
            throw new IllegalArgumentException("City name must not be empty");
        }

        String url = String.format("%s/weather?q=%s&appid=%s&units=metric",
                config.getUrl(), cityName.trim(), config.getKey());

        try {
            log.info("Weather request in process for city: {}", cityName);
            WeatherByNameResponseDto response = restTemplate.getForObject(url, WeatherByNameResponseDto.class);
            if (response == null) {
                log.warn("Answer from OpenWeather is empty for city: {}", cityName);
                throw new RuntimeException("Weather data error");
            }
            log.info("Successful answer from OpenWeather for city: {}", cityName);
            return response;
        } catch (RestClientException e) {
            log.error("Error request OpenWeather API for city {}: {}", cityName, e.getMessage(), e);
            throw new RuntimeException("Error receiving data from OpenWeather API.");
        }
    }


}
