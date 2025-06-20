package by.egorivanov.weather.model.common.weather;

import com.fasterxml.jackson.annotation.JsonProperty;


public record Weather(@JsonProperty("main") String description,
                      @JsonProperty("icon") String icon) {
}
