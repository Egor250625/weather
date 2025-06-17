package by.egorivanov.weather.model.common.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Coordinates(@JsonProperty("lon") double longitude,
                          @JsonProperty("lat") double latitude) {
}
