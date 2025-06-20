package by.egorivanov.weather.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherByNameResponseDto {
    @JsonProperty("name")
    private String name;

    @JsonProperty("lat")
    private double latitude;

    @JsonProperty("lon")
    private double longitude;

    @JsonProperty("country")
    private String country;

    @JsonProperty("state")
    private String state;
}
