package by.egorivanov.weather.model.common.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Main(@JsonProperty("temp") String temperature,
                   @JsonProperty("feels_like") String feelsLike,
                   @JsonProperty("humidity") String humidity) {
}
