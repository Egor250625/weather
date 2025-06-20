package by.egorivanov.weather.repository;

import by.egorivanov.weather.model.entity.Sessions;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Sessions, UUID> {


    Optional<Sessions> findById(UUID id);


    @Transactional
    @Modifying
    @Query("DELETE FROM Sessions s WHERE s.expiresAt  < :cutoff")
    void deleteExpiredSessions(@Param("cutoff") LocalDateTime cutoff);

    Sessions findSessionByUserId(int userId);
}
