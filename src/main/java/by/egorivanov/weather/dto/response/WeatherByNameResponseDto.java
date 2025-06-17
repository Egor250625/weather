package by.egorivanov.weather.dto.response;

import by.egorivanov.weather.model.common.weather.Coordinates;
import by.egorivanov.weather.model.common.weather.Main;
import by.egorivanov.weather.model.common.weather.Sys;
import by.egorivanov.weather.model.common.weather.Weather;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherByNameResponseDto {
    @JsonProperty("name")
    private String name;

    @JsonProperty("coord")
    private Coordinates coordinates;

    @JsonProperty("weather")
    private List<Weather> weather;

    @JsonProperty("main")
    private Main main;

    @JsonProperty("sys")
    private Sys sys;
}
