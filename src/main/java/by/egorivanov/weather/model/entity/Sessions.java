package by.egorivanov.weather.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "sessions",schema = "weather")
public class Sessions implements BaseEntity<UUID> {

    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private int userId;


    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
}
