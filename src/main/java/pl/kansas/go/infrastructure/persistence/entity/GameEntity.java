package pl.kansas.go.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Database Spring Game entity.
 */
@Entity
@Table(name = "games")
public class GameEntity {
    @Id
    private UUID id;
    private LocalDateTime startDate;

    public GameEntity() {
    }

    public GameEntity(UUID id, LocalDateTime startDate) {
        this.id = id;
        this.startDate = startDate;
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }
}
