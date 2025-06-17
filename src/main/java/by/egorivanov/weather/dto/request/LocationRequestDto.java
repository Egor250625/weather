package by.egorivanov.weather.dto.request;

public record LocationRequestDto(
        String name,
        Integer userId,
        Double latitude,
        Double longitude
) {
}
