package by.egorivanov.weather.mapper;

import by.egorivanov.weather.dto.request.UserAuthDto;
import by.egorivanov.weather.dto.request.UserCreateEditDto;
import by.egorivanov.weather.dto.response.UserReadDto;
import by.egorivanov.weather.model.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper  {

    UserReadDto toDto(Users users);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "locations",ignore = true)
    Users toEntity(UserCreateEditDto dto);


    UserAuthDto toAuthDto(Users users);
}
