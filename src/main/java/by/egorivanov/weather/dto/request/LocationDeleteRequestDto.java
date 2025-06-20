package by.egorivanov.weather.dto.request;

public record LocationDeleteRequestDto(
        Integer userId,
        Double latitude,
        Double longitude
) {
}
