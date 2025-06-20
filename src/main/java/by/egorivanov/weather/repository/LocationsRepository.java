package by.egorivanov.weather.repository;

import by.egorivanov.weather.model.entity.Locations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LocationsRepository extends JpaRepository<Locations,Integer> {
    List<Locations> findByUsersId(int userId);

    @Query("SELECT l FROM Locations l WHERE l.users.id = :userId AND " +
           "ABS(l.latitude - :lat) < 0.01 AND ABS(l.longitude - :lon) < 0.01")
    Optional<Locations> findByUsers_IdAndLatitudeAndLongitude(@Param("userId") Integer usersId,
                                                              @Param("lat") double latitude,
                                                              @Param("lon") double longitude);
    //Ошибка при удалении из таблицы,No argument for named parameter ':userId'] with

    boolean existsByUsers_IdAndLatitudeAndLongitude(Integer usersId, double latitude, double longitude);
}
