package by.egorivanov.weather.dto.response;

public record LocationResponseDto(
        String name,
        Integer userId,
        Double latitude,
        Double longitude
) {
}
