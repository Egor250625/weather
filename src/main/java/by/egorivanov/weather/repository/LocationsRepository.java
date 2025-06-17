package by.egorivanov.weather.repository;

import by.egorivanov.weather.model.entity.Locations;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationsRepository extends JpaRepository<Locations,Integer> {
    List<Locations> findByUsersId(int userId);

}
