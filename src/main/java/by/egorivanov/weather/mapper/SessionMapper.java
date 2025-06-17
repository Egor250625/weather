package by.egorivanov.weather.mapper;

import by.egorivanov.weather.dto.response.SessionDto;
import by.egorivanov.weather.model.entity.Sessions;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SessionMapper {


    Sessions toEntity(SessionDto dto);

    @Mapping(source = "expiresAt", target = "expiresAt")
    @Mapping(source = "id", target = "sessionId")
    SessionDto toDto(Sessions sessions);
}
