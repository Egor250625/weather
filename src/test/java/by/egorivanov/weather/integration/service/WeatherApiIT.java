package by.egorivanov.weather.integration.service;

import by.egorivanov.weather.config.OpenWeatherConfig;
import by.egorivanov.weather.dto.response.WeatherByNameResponseDto;
import by.egorivanov.weather.integration.annotations.IT;
import by.egorivanov.weather.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@IT
public class WeatherApiIT {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private OpenWeatherConfig config;

    @InjectMocks
    private WeatherService weatherService;

    @Test
    void getWeatherByCityName(){
        String cityName = "Tbilisi";
        String url = "http://api.openweathermap.org/data/2.5/weather?q=Tbilisi&appid=key&units=metric";

        WeatherByNameResponseDto mockResponse = new WeatherByNameResponseDto();
        mockResponse.setName(cityName);

        when(config.getUrl()).thenReturn("http://api.openweathermap.org/data/2.5");
        when(config.getKey()).thenReturn("key");
        when(restTemplate.getForObject(url,WeatherByNameResponseDto.class)).thenReturn(mockResponse);

        WeatherByNameResponseDto response = weatherService.getWeatherByCityName(cityName);

        assertNotNull(response);
        assertEquals(cityName, response.getName());

        verify(restTemplate).getForObject(url, WeatherByNameResponseDto.class);
    }

    @Test
    void getWeatherByCityName_restClientException() {
        String cityName = "Tbilisi";
        String url = "http://api.openweathermap.org/data/2.5/weather?q=Tbilisi&appid=key&units=metric";

        when(config.getUrl()).thenReturn("http://api.openweathermap.org/data/2.5");
        when(config.getKey()).thenReturn("key");
        when(restTemplate.getForObject(url, WeatherByNameResponseDto.class))
                .thenThrow(new RestClientException("API error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> weatherService.getWeatherByCityName(cityName));
        assertEquals("Error receiving data from OpenWeather API.", ex.getMessage());
    }

    @Test
    void getWeatherByCoordinates_invalidLatitudeException() {
        double invalidLat = 100.0;
        double lon = 50.0;

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                weatherService.getWeatherByCoordinates(invalidLat, lon));
        assertEquals("Wrong coordinates", ex.getMessage());
    }

}
