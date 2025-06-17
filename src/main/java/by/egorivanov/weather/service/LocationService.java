package by.egorivanov.weather.service;

import by.egorivanov.weather.dto.request.LocationRequestDto;
import by.egorivanov.weather.dto.response.WeatherByCoordinateResponseDto;
import by.egorivanov.weather.mapper.LocationMapper;
import by.egorivanov.weather.model.entity.Locations;
import by.egorivanov.weather.repository.LocationsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocationService {
    private final WeatherService weatherService;
    private final LocationsRepository locationsRepository;
    private final LocationMapper locationMapper;

    public void save(LocationRequestDto locationRequestDto) {
        try {
            locationsRepository.save(locationMapper.toEntity(locationRequestDto));
            log.info("Location {} successfully added", locationRequestDto.name());
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Location with name " + locationRequestDto.name() + " already saved");
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

    public void delete(LocationRequestDto locationRequestDto) {
        try{
            locationsRepository.delete(locationMapper.toEntity(locationRequestDto));
        }catch (EntityNotFoundException e ){
            throw new EntityNotFoundException("Delete error " +locationRequestDto.name());
        }
    }


}
