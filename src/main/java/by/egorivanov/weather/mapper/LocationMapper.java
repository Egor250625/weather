package by.egorivanov.weather.mapper;

import by.egorivanov.weather.dto.request.LocationRequestDto;
import by.egorivanov.weather.dto.response.LocationResponseDto;
import by.egorivanov.weather.model.entity.Locations;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    LocationResponseDto toDto(Locations locations);


    @Mapping(target = "id",ignore = true)
    Locations toEntity(LocationRequestDto locationRequestDto);
}
