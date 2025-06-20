package by.egorivanov.weather.service;

import by.egorivanov.weather.dto.request.LocationDeleteRequestDto;
import by.egorivanov.weather.dto.request.LocationRequestDto;
import by.egorivanov.weather.dto.response.WeatherByCoordinateResponseDto;
import by.egorivanov.weather.mapper.LocationMapper;
import by.egorivanov.weather.model.entity.Locations;
import by.egorivanov.weather.model.entity.Users;
import by.egorivanov.weather.repository.LocationsRepository;
import by.egorivanov.weather.repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocationService {
    private final WeatherService weatherService;
    private final LocationsRepository locationsRepository;
    private final LocationMapper locationMapper;
    private final UsersRepository usersRepository;

    @Transactional
    public void save(LocationRequestDto dto) {
        Users user = usersRepository.findById(dto.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        try {
            Locations location = new Locations();
            Locations locationEntity = locationMapper.toEntity(dto);
            locationEntity.setUsers(user);
            locationsRepository.save(locationEntity);
            log.info("Location {} successfully added", dto.name());
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Location with name " + dto.name() + " already saved");
        }
    }


    public List<WeatherByCoordinateResponseDto> findLocationsByUserId(int userId) {
        try {
            var locations = locationsRepository.findByUsersId(userId);
            List<WeatherByCoordinateResponseDto> weathers = new ArrayList<>();
            for (Locations location : locations) {
                var lat = location.getLatitude();
                var lon = location.getLongitude();
                weathers.add(weatherService.getWeatherByCoordinates(lat, lon));
            }
            return weathers;
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("No locations found for user " + userId);
        }
    }

    @Transactional
    public void delete(LocationDeleteRequestDto dto) {
        log.info("Delete location process started with lat = {},lon = {}", dto.latitude(), dto.longitude());
        Users user = usersRepository.findById(dto.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        log.info("User {} found", user.getUsername());
        try {
            var location = locationsRepository.findByUsers_IdAndLatitudeAndLongitude(user.getId(),
                    dto.latitude(), dto.longitude()).orElseThrow(() -> new IllegalArgumentException("Location not found"));
            ;
            location.setUsers(user);

            user.getLocations().remove(location);
            // locationsRepository.delete(location); Не получилось использовать через дто пришлось вызывать напрямую у юзера
            log.info("Delete success>");
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Delete error with this coordinates.");
        }
    }

    public boolean existByParameters(LocationRequestDto dto) {
        return locationsRepository.existsByUsers_IdAndLatitudeAndLongitude(dto.userId(), dto.latitude(), dto.longitude());
    }


}
